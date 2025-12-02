package dev.ngb.base_hub.common.infra.jdbc.entity.organization;

import dev.ngb.base_hub.common.domain.constant.OrganizationStatus;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Table("organizations")
@Builder
public record OrganizationEntity(
        @Id Long id,
        String name,
        String code,
        String domain,
        String contact,
        String description,
        OrganizationStatus status,
        UUID createdById,
        UUID updatedById,
        Instant createdAt,
        Instant updatedAt
) {
}
