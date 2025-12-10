package dev.ngb.base_hub.infrastructure.command;

import dev.ngb.base_hub.application.use_case.Command;
import dev.ngb.base_hub.application.use_case.UseCaseHandler;
import dev.ngb.base_hub.application.spi.use_case.UseCaseMediator;
import dev.ngb.base_hub.infrastructure.mediator.BaseMediator;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class UseCaseMediatorImpl extends BaseMediator<UseCaseHandler<?, ?>>
        implements UseCaseMediator {

    public UseCaseMediatorImpl(ApplicationContext context) {
        super(context, UseCaseHandler.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R> R execute(Command<R> command) {
        UseCaseHandler<Command<R>, R> handler =
                (UseCaseHandler<Command<R>, R>) getHandler(command.getClass());

        if (handler == null) {
            throw new IllegalStateException("No handler for: " + command.getClass());
        }

        return handler.execute(command);
    }
}
