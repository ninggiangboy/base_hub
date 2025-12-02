package dev.ngb.base_hub.common.infra.impl.event;

import dev.ngb.base_hub.common.api.event.EventMediator;
import dev.ngb.base_hub.base.annotation.InfraService;
import dev.ngb.base_hub.base.event.Event;
import dev.ngb.base_hub.base.event.EventHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.core.GenericTypeResolver;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@InfraService
public class EventMediatorImpl implements EventMediator {

    private final Map<Class<? extends Event>, EventHandler<? extends Event>> handlers;

    @SuppressWarnings("unchecked")
    public EventMediatorImpl(ApplicationContext context) {
        Map<Class<? extends Event>, EventHandler<? extends Event>> registry = new HashMap<>();

        context.getBeansOfType(EventHandler.class)
                .values()
                .forEach(handler -> {
                    Class<? extends Event> eventType = resolveEventType(handler);

                    if (eventType == null) {
                        throw new IllegalStateException(
                                "Cannot resolve generic type for handler: " + handler.getClass()
                        );
                    }

                    registry.put(eventType, handler);
                });

        this.handlers = Collections.unmodifiableMap(registry);
    }

    @SuppressWarnings("unchecked")
    private Class<? extends Event> resolveEventType(EventHandler<? extends Event> handler) {
        return (Class<? extends Event>) GenericTypeResolver.resolveTypeArgument(
                handler.getClass(),
                EventHandler.class
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handle(Event event) {
        EventHandler<Event> handler = (EventHandler<Event>) handlers.get(event.getClass());
        if (handler == null) {
            throw new IllegalStateException("No handler found for event type: " + event.getClass());
        }
        handler.handle(event);
    }
}
