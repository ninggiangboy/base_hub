package dev.ngb.base_hub.common.infra.jdbc.entity.user;

import dev.ngb.base_hub.domain.user.constant.OrganizationUserRole;
import dev.ngb.base_hub.domain.user.constant.OrganizationUserStatus;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Table("organization_user")
@Builder
public record OrganizationUserEntity(
        @Id UUID id,
        String loginId,
        String email,
        String displayName,
        String hashedPassword,
        String credentialToken,
        Instant lastLoginAt,
        OrganizationUserStatus status,
        OrganizationUserRole role,
        UUID createdById,
        UUID updatedById,
        Instant createdAt,
        Instant updatedAt
) {
}
