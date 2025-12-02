package dev.ngb.base_hub.base.domain;

import lombok.Getter;

public class Result<T> {

    @Getter
    private final boolean isSuccess;
    private final T data;
    private final DomainError error;

    private Result(boolean isSuccess, T data, DomainError error) {
        this.isSuccess = isSuccess;
        this.data = data;
        this.error = error;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(true, data, null);
    }

    public static Result<Void> success() {
        return new Result<>(true, null, null);
    }

    public static <T> Result<T> failure(DomainError error) {
        return new Result<>(false, null, error);
    }

    public static <T> Result<T> failure(DomainError error, String message) {
        return new Result<>(false, null, error);
    }

    public T get() {
        if (!isSuccess) {
            throw new IllegalStateException("Cannot get data from a failed result.");
        }
        return data;
    }

    public DomainError getError() {
        if (isSuccess) {
            throw new IllegalStateException("Cannot get error from a successful result.");
        }
        return error;
    }

    @Override
    public String toString() {
        return isSuccess
                ? "Result{success, data=" + data + "}"
                : "Result{failure, error='" + error + "'}";
    }
}
