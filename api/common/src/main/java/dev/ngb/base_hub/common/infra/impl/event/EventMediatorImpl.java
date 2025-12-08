package dev.ngb.base_hub.common.infra.impl.event;

import dev.ngb.base_hub.base.annotation.InfraService;
import dev.ngb.base_hub.base.event.Event;
import dev.ngb.base_hub.base.event.EventHandler;
import dev.ngb.base_hub.common.api.event.EventMediator;
import dev.ngb.base_hub.common.infra.impl.mediator.BaseMediator;
import org.springframework.context.ApplicationContext;

@InfraService
public class EventMediatorImpl extends BaseMediator<EventHandler<?>>
        implements EventMediator {

    public EventMediatorImpl(ApplicationContext context) {
        super(context, EventHandler.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handle(Event event) {
        EventHandler<Event> handler = (EventHandler<Event>) getHandler(event.getClass());

        if (handler == null) {
            throw new IllegalStateException(
                    "No handler for event type: " + event.getClass()
            );
        }

        handler.handle(event);
    }
}

