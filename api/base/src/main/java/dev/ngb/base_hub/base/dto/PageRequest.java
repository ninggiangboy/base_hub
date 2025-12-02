package dev.ngb.base_hub.base.dto;

/**
 * Immutable record representing pagination and sorting parameters for queries.
 * Automatically applies sensible defaults and constraints to prevent invalid configurations.
 */
public record PageRequest(
        int pageNumber,
        int pageSize,
        String sortField,
        SortDirection sortDirection
) {

    public enum SortDirection {
        ASC,
        DESC
    }

    private static final int DEFAULT_PAGE_NUMBER = 1;
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 500;

    public static PageRequest unpaged() {
        return new PageRequest(DEFAULT_PAGE_NUMBER, MAX_PAGE_SIZE, null, null);
    }

    public static PageRequest unpaged(String sortField, SortDirection direction) {
        return new PageRequest(DEFAULT_PAGE_NUMBER, MAX_PAGE_SIZE, sortField, direction);
    }

    public static PageRequest defaultPaged() {
        return new PageRequest(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE, null, null);
    }

    public PageRequest {
        pageNumber = normalizePageNumber(pageNumber);
        pageSize = normalizePageSize(pageSize);
        sortDirection = normalizeSortDirection(sortField, sortDirection);
    }

    public PageRequest withSort(String sortField, SortDirection direction) {
        return new PageRequest(pageNumber, pageSize, sortField, direction);
    }

    public PageRequest withSortAsc(String sortField) {
        return withSort(sortField, SortDirection.ASC);
    }

    public PageRequest withSortDesc(String sortField) {
        return withSort(sortField, SortDirection.DESC);
    }

    public int calculateOffset() {
        return (pageNumber - 1) * pageSize;
    }

    public boolean hasSorting() {
        return sortField != null;
    }

    private static int normalizePageNumber(int pageNumber) {
        return Math.max(pageNumber, DEFAULT_PAGE_NUMBER);
    }

    private static int normalizePageSize(int pageSize) {
        if (pageSize <= 0) {
            return DEFAULT_PAGE_SIZE;
        }
        return Math.min(pageSize, MAX_PAGE_SIZE);
    }

    private static SortDirection normalizeSortDirection(String sortField, SortDirection direction) {
        if (sortField != null && direction == null) {
            return SortDirection.ASC;
        }
        return direction;
    }
}