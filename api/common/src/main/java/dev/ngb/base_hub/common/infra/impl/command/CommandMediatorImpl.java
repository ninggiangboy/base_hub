package dev.ngb.base_hub.common.infra.impl.command;

import dev.ngb.base_hub.base.annotation.InfraService;
import dev.ngb.base_hub.base.command.Command;
import dev.ngb.base_hub.base.command.CommandHandler;
import dev.ngb.base_hub.common.api.command.CommandMediator;
import dev.ngb.base_hub.common.infra.impl.mediator.BaseMediator;
import org.springframework.context.ApplicationContext;

@InfraService
public class CommandMediatorImpl extends BaseMediator<CommandHandler<?, ?>>
        implements CommandMediator {

    public CommandMediatorImpl(ApplicationContext context) {
        super(context, CommandHandler.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R> R handle(Command<R> command) {
        CommandHandler<Command<R>, R> handler =
                (CommandHandler<Command<R>, R>) getHandler(command.getClass());

        if (handler == null) {
            throw new IllegalStateException("No handler for: " + command.getClass());
        }

        return handler.execute(command);
    }
}
