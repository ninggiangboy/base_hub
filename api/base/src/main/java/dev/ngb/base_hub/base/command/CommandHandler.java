package dev.ngb.base_hub.base.command;

import dev.ngb.base_hub.base.domain.Result;

public interface CommandHandler<C extends Command<R>, R> {
    Result<R> execute(C command);
}
