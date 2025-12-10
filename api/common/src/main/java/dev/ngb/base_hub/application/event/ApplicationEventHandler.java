package dev.ngb.base_hub.application.event;

public interface ApplicationEventHandler<T extends ApplicationEvent> {
    void handle(T event);
}
