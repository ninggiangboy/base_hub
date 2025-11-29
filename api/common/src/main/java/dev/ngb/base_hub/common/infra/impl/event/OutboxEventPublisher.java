package dev.ngb.base_hub.common.infra.impl.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ngb.base_hub.common.api.event.EventPublisher;
import dev.ngb.base_hub.common.api.tenant.OrganizationContextHolder;
import dev.ngb.base_hub.common.base.annotation.InfraService;
import dev.ngb.base_hub.common.infra.outbox.OutboxEventEntity;
import dev.ngb.base_hub.common.infra.outbox.OutboxEventRepository;
import dev.ngb.base_hub.common.infra.outbox.OutboxEventStatus;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@InfraService
@RequiredArgsConstructor
public class OutboxEventPublisher implements EventPublisher {

    private final OutboxEventRepository outboxEventRepository;
    private final OrganizationContextHolder organizationContextHolder;
    private final ObjectMapper objectMapper;

    @Override
    public void publish(Object event) {
        try {
            OutboxEventEntity outboxEvent = OutboxEventEntity.builder()
                    .type(event.getClass().getSimpleName())
                    .payload(objectMapper.writeValueAsString(event))
                    .tenantId(organizationContextHolder.getCurrentOrgId())
                    .status(OutboxEventStatus.PENDING)
                    .createdAt(Instant.now())
                    .build();
            outboxEventRepository.save(outboxEvent);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize event", e);
        }
    }
}
