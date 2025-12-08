package dev.ngb.base_hub.base.event;

public interface EventHandler<T extends Event> {
    void handle(T event);
}
