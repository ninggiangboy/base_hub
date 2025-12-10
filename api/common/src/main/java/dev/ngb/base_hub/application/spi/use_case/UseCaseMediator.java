package dev.ngb.base_hub.application.spi.use_case;

import dev.ngb.base_hub.application.use_case.Command;

public interface UseCaseMediator {
    <R> R execute(Command<R> command);
}
