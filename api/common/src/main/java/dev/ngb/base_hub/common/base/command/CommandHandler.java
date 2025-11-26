package dev.ngb.base_hub.common.base.command;

import dev.ngb.base_hub.common.base.domain.Result;

public interface CommandHandler<C extends Command<R>, R> {
    Result<R> execute(C command);
}
