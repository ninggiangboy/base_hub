package dev.ngb.base_hub.application.spi.event;

public interface EventPublisher {
    void publish(Object event);
}
