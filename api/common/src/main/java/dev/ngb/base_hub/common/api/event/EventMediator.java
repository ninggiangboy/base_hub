package dev.ngb.base_hub.common.api.event;

import dev.ngb.base_hub.base.event.Event;

public interface EventMediator {
    void handle(Event event);
}
