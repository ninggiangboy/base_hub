package dev.ngb.base_hub.infrastructure.mediator;

import org.springframework.context.ApplicationContext;
import org.springframework.core.GenericTypeResolver;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseMediator<THandler> {

    private final Map<Class<?>, THandler> handlers;

    @SuppressWarnings("unchecked")
    protected BaseMediator(ApplicationContext context, Class<? super THandler> handlerClass) {
        Map<Class<?>, THandler> registry = new HashMap<>();

        context.getBeansOfType((Class<THandler>) handlerClass)
                .values()
                .forEach(handler -> {
                    Class<?> type = resolveGeneric(handler, handlerClass);
                    if (type == null) {
                        throw new IllegalStateException(
                                "Cannot resolve generic type for handler: " + handler.getClass()
                        );
                    }
                    registry.put(type, handler);
                });

        this.handlers = Collections.unmodifiableMap(registry);
    }

    protected Class<?> resolveGeneric(THandler handler, Class<?> handlerBaseClass) {
        Class<?>[] generics =
                GenericTypeResolver.resolveTypeArguments(handler.getClass(), handlerBaseClass);
        return generics.length == 0 ? null : generics[0];
    }

    protected THandler getHandler(Class<?> type) {
        return handlers.get(type);
    }
}
