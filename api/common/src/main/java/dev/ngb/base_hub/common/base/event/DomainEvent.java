package dev.ngb.base_hub.common.base.event;

public interface DomainEvent {
    default String orgId() {
        return null;
    }
}