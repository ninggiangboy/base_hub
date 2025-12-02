package dev.ngb.base_hub.base.dto;

import dev.ngb.base_hub.domain.constant.UserType;

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
