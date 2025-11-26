package dev.ngb.base_hub.admin.shared.tenant.event;

import dev.ngb.base_hub.common.base.event.IntegrationEvent;

public record TenantCreatedEvent(
        String tenantId,
        String adminName,
        String adminEmail
) implements IntegrationEvent {
}
