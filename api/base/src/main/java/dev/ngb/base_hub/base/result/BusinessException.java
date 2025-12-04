package dev.ngb.base_hub.base.result;

import dev.ngb.base_hub.base.domain.DomainError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class BusinessException extends RuntimeException {
    private final DomainError error;
}
