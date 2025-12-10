package dev.ngb.base_hub.application.spi.event;

import dev.ngb.base_hub.application.event.ApplicationEvent;

public interface EventMediator {
    void handle(ApplicationEvent event);
}
