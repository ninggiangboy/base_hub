package dev.ngb.base_hub.base.command;

public interface CommandHandler<C extends Command<R>, R> {
    R execute(C command);
}
