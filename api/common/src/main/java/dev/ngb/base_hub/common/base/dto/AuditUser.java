package dev.ngb.base_hub.common.base.dto;

import dev.ngb.base_hub.common.domain.constant.UserType;

import java.time.Instant;
import java.util.UUID;

public record AuditUser(
        UUID id,
        String name,
        UserType userType,
        Instant timestamp,
        String auditId,
        Boolean isDeleted
) {
}
