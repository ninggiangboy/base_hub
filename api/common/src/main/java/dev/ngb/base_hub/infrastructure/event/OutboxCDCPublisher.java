package dev.ngb.base_hub.infrastructure.event;

import dev.ngb.base_hub.application.spi.tenant.TenantContextService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import dev.ngb.base_hub.application.spi.event.EventPublisher;
import dev.ngb.base_hub.common.annotation.Topic;
import dev.ngb.base_hub.infrastructure.persistence.entity.OutboxEventEntity;
import dev.ngb.base_hub.infrastructure.persistence.repository.OutboxEventJdbcRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.annotation.KafkaListener;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxCDCPublisher implements EventPublisher {

    private final OutboxEventJdbcRepository outboxEventJdbcRepository;
    private final TenantContextService tenantContextService;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publish(Object event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            log.info("Saving outbox event: {}", payload);
            OutboxEventEntity outboxEvent = OutboxEventEntity.builder()
                    .type(event.getClass().getName())
                    .payload(payload)
                    .orgId(tenantContextService.getTenantId())
                    .createdAt(Instant.now())
                    .build();
            outboxEventJdbcRepository.save(outboxEvent);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize event", e);
        }
    }

    @KafkaListener(topics = "cdc.public.outbox_events", containerFactory = "kafkaListenerFactoryString")
    public void handleOutboxEvent(String payload) throws ClassNotFoundException {
        JsonNode node = objectMapper.readTree(payload);
        JsonNode after = node.get("payload").get("after");
        if (after != null && !after.isNull()) {
            OutboxEventEntity event = objectMapper.treeToValue(after, OutboxEventEntity.class);
            Class<?> eventClass = Class.forName(event.type());
            Topic annotation = eventClass.getAnnotation(Topic.class);
            if (annotation == null) {
                throw new IllegalArgumentException("No @KafkaTopic annotation found on " + event.type());
            }
            String topic = annotation.value();
            log.info("Publishing event topic: {}, payload: {}", topic, event.payload());
            ProducerRecord<String, Object> record = new ProducerRecord<>(topic, event.id().toString(), event.payload());
            record.headers().add(new RecordHeader("__TypeId__", eventClass.getName().getBytes(StandardCharsets.UTF_8)));
            kafkaTemplate.send(record);
        }
    }
}
