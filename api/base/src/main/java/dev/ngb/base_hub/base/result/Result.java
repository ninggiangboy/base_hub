package dev.ngb.base_hub.base.result;

import dev.ngb.base_hub.base.domain.DomainError;

public record Result<T>(boolean isSuccess, T data, DomainError error) {

    public static <T> Result<T> success(T data) {
        return new Result<>(true, data, null);
    }

    public static Result<Void> success() {
        return new Result<>(true, null, null);
    }

    public static <T> Result<T> failure(DomainError error) {
        if (error == null) throw new IllegalArgumentException("Error cannot be null");
        return new Result<>(false, null, error);
    }

    public static void throwError(DomainError error) {
        if (error == null) throw new IllegalArgumentException("Error cannot be null");
        throw new BusinessException(error);
    }

    public T get() {
        if (!isSuccess) throw new IllegalStateException("Cannot get data from a failed result.");
        return data;
    }

    public DomainError getError() {
        if (isSuccess) throw new IllegalStateException("Cannot get error from a successful result.");
        return error;
    }

    public void throwIfFailure() throws BusinessException {
        if (isSuccess) throw new BusinessException(error);
    }

    public boolean isFailure() {
        return !isSuccess;
    }
}
