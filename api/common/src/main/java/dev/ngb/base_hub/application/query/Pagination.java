package dev.ngb.base_hub.application.query;

import java.util.function.Supplier;

public record Pagination(Integer number, Integer size, Long totalItems) {

    public static Pagination of(PageRequest page, Supplier<Long> countProvider) {
        Long totalItems = countProvider.get();
        return new Pagination(page.pageNumber(), page.pageSize(), totalItems);
    }

    public static Pagination of(PageRequest page, Long totalItems) {
        return new Pagination(page.pageNumber(), page.pageSize(), totalItems);
    }

    public Integer totalPages() {
        return (int) Math.ceil((double) totalItems / size);
    }

    public Boolean hasNext() {
        return number < totalPages();
    }

    public Boolean hasPrevious() {
        return number > 1;
    }
}
