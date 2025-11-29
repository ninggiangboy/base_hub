package dev.ngb.base_hub.admin.shared.organization.event;

import dev.ngb.base_hub.common.base.event.IntegrationEvent;

public record OrganizationCreatedEvent(
        String orgId,
        String adminName,
        String adminEmail
) implements IntegrationEvent {
}
