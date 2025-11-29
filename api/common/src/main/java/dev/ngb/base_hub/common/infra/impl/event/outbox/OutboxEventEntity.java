package dev.ngb.base_hub.common.infra.impl.event.outbox;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Table(name = "outbox_event")
@Builder
public record OutboxEventEntity(
        @Id UUID id,
        String type,
        String payload,
        String orgId,
        Instant createdAt
) {
}