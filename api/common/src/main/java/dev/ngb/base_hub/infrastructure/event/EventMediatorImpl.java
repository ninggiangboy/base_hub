package dev.ngb.base_hub.infrastructure.event;

import dev.ngb.base_hub.application.event.ApplicationEvent;
import dev.ngb.base_hub.application.event.ApplicationEventHandler;
import dev.ngb.base_hub.application.spi.event.EventMediator;
import dev.ngb.base_hub.infrastructure.mediator.BaseMediator;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class EventMediatorImpl extends BaseMediator<ApplicationEventHandler<?>>
        implements EventMediator {

    public EventMediatorImpl(ApplicationContext context) {
        super(context, ApplicationEventHandler.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handle(ApplicationEvent event) {
        ApplicationEventHandler<ApplicationEvent> handler = (ApplicationEventHandler<ApplicationEvent>) getHandler(event.getClass());

        if (handler == null) {
            throw new IllegalStateException(
                    "No handler for event type: " + event.getClass()
            );
        }

        handler.handle(event);
    }
}

