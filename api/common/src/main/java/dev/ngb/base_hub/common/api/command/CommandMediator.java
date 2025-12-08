package dev.ngb.base_hub.common.api.command;

import dev.ngb.base_hub.base.command.Command;

public interface CommandMediator {
    <R> R handle(Command<R> command);
}
