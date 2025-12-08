package dev.ngb.base_hub.common.infra.jdbc.entity.organization;

import dev.ngb.base_hub.domain.organization.constant.OrganizationStatus;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.Map;

@Table(value = "organizations", schema = "public")
@Builder
public record OrganizationEntity(
        @Id Long id,
        String name,
        String code,
        String domain,
        String contact,
        String description,
        OrganizationStatus status,
        Map<String, Object> configuration,
        @Version
        Integer version,
        String createdById,
        String updatedById,
        String deletedById,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {
}
