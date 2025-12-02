package dev.ngb.base_hub.admin.shared.organization.event;

import dev.ngb.base_hub.common.base.annotation.Topic;
import dev.ngb.base_hub.common.base.event.IntegrationEvent;

@Topic(OrganizationCreatedEvent.TOPIC)
public record OrganizationCreatedEvent(
        String orgId,
        String adminName,
        String adminEmail
) implements IntegrationEvent {
    public static final String TOPIC = "events.organization.created";
}