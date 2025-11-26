package dev.ngb.base_hub.common.base.event;

public interface IntegrationEventHandler<T extends IntegrationEvent> {
    void handle(T event);
}
