package dev.ngb.base_hub.application.use_case;

public interface UseCaseHandler<C extends Command<R>, R> {
    R execute(C command);
}
