package dev.ngb.base_hub.base.event;

public interface Event {
    default String orgId() {
        return null;
    }
}
