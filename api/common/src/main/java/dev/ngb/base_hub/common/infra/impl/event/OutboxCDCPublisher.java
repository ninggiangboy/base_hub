package dev.ngb.base_hub.common.infra.impl.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ngb.base_hub.common.api.event.EventPublisher;
import dev.ngb.base_hub.common.api.tenant.OrganizationContextHolder;
import dev.ngb.base_hub.common.base.annotation.InfraService;
import dev.ngb.base_hub.common.infra.impl.event.outbox.OutboxEventEntity;
import dev.ngb.base_hub.common.infra.impl.event.outbox.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.Instant;

@InfraService
@RequiredArgsConstructor
public class OutboxCDCPublisher implements EventPublisher {

    private final OutboxEventRepository outboxEventRepository;
    private final OrganizationContextHolder organizationContextHolder;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publish(Object event) {
        try {
            OutboxEventEntity outboxEvent = OutboxEventEntity.builder()
                    .type(event.getClass().getSimpleName())
                    .payload(objectMapper.writeValueAsString(event))
                    .orgId(organizationContextHolder.getCurrentOrgId())
                    .createdAt(Instant.now())
                    .build();
            outboxEventRepository.save(outboxEvent);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize event", e);
        }
    }

    @KafkaListener(topics = "cdc.public.outbox_event")
    public void handleOutboxEvent(String payload) throws JsonProcessingException {
        JsonNode node = objectMapper.readTree(payload);
        JsonNode after = node.get("after");
        if (after != null && !after.isNull()) {
            OutboxEventEntity event = objectMapper.treeToValue(after, OutboxEventEntity.class);
            kafkaTemplate.send("events-topic", event.id().toString(), event.payload());
        }
    }
}
