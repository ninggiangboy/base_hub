package dev.ngb.base_hub.common.base.event;

public interface IntegrationEvent {
    default String orgId() {
        return null;
    }
}
