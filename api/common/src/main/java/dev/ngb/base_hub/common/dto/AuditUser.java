package dev.ngb.base_hub.common.dto;

import dev.ngb.base_hub.common.constant.UserType;

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
