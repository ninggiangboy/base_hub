package dev.ngb.base_hub.common.infra.impl.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ngb.base_hub.common.api.event.EventPublisher;
import dev.ngb.base_hub.common.base.annotation.Topic;
import dev.ngb.base_hub.common.context.OrganizationContextHolder;
import dev.ngb.base_hub.common.base.annotation.InfraService;
import dev.ngb.base_hub.common.infra.jdbc.entity.OutboxEventEntity;
import dev.ngb.base_hub.common.infra.jdbc.repository.OutboxEventJdbcRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.data.convert.TypeMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

@Slf4j
@InfraService
@RequiredArgsConstructor
public class OutboxCDCPublisher implements EventPublisher {

    private final OutboxEventJdbcRepository outboxEventJdbcRepository;
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
                    .orgId(OrganizationContextHolder.getCurrentOrgId())
                    .createdAt(Instant.now())
                    .build();
            outboxEventJdbcRepository.save(outboxEvent);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize event", e);
        }
    }

    @KafkaListener(topics = "cdc.public.outbox_events", containerFactory = "kafkaListenerFactoryString")
    public void handleOutboxEvent(String payload) throws JsonProcessingException, ClassNotFoundException {
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
