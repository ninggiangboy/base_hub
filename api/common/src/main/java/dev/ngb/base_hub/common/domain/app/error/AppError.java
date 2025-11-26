package dev.ngb.base_hub.common.domain.app.error;

import dev.ngb.base_hub.common.base.domain.DomainError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AppError implements DomainError {
    ;

    private final String message;
}
