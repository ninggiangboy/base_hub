package dev.ngb.base_hub.common.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Encapsulates query parameters including filters, pagination, and sorting.
 * Validates filter and sort fields against allowed lists to prevent SQL injection
 * and unauthorized field access.
 */
@Slf4j
@Getter
public class QueryContext {

    private final Map<String, Object> filters;
    private final PageRequest pageRequest;

    @Builder
    private QueryContext(
            Map<String, Object> filters,
            PageRequest pageRequest,
            List<String> allowedFilterFields,
            List<String> allowedSortFields
    ) {
        List<String> safeFilterFields = Optional.ofNullable(allowedFilterFields)
                .orElseGet(Collections::emptyList);
        List<String> safeSortFields = Optional.ofNullable(allowedSortFields)
                .orElseGet(Collections::emptyList);

        this.pageRequest = initializePageRequest(pageRequest, safeSortFields);
        this.filters = sanitizeFilters(filters, safeFilterFields);
    }

    /**
     * Builds a QueryContext using enum classes to define allowed fields.
     * This provides compile-time safety for field names.
     */
    public static <F extends Enum<F>, S extends Enum<S>> QueryContext of(
            Map<String, Object> filters,
            PageRequest pageRequest,
            Class<F> filterFieldEnum,
            Class<S> sortFieldEnum
    ) {
        return QueryContext.builder()
                .filters(filters)
                .pageRequest(pageRequest)
                .allowedFilterFields(extractEnumNames(filterFieldEnum))
                .allowedSortFields(extractEnumNames(sortFieldEnum))
                .build();
    }

    /**
     * Builds a simple QueryContext without filter/sort field restrictions.
     * Use with caution - prefer the enum-based factory method for better security.
     */
    public static QueryContext unrestricted(Map<String, Object> filters, PageRequest pageRequest) {
        return QueryContext.builder()
                .filters(filters)
                .pageRequest(pageRequest)
                .build();
    }

    /**
     * Converts the query context to a map suitable for named parameter queries.
     * Includes pagination, sorting, and filter parameters.
     */
    public Map<String, Object> toNamedParameterMap() {
        Map<String, Object> params = new HashMap<>(filters);
        params.put("sortField", pageRequest.sortField());
        params.put("sortDirection", pageRequest.sortDirection());
        params.put("limit", pageRequest.pageSize());
        params.put("offset", pageRequest.calculateOffset());
        return params;
    }

    private static PageRequest initializePageRequest(
            PageRequest pageRequest,
            List<String> allowedSortFields
    ) {
        PageRequest result = Optional.ofNullable(pageRequest)
                .orElse(PageRequest.unpaged());

        // Apply default sort field if none provided and allowed fields exist
        if (!result.hasSorting() && !allowedSortFields.isEmpty()) {
            String defaultSortField = allowedSortFields.getFirst();
            result = result.withSort(defaultSortField, result.sortDirection());
        }

        // Validate sort field against allowed list
        if (result.hasSorting() && !allowedSortFields.isEmpty()
                && !allowedSortFields.contains(result.sortField())) {
            log.warn("Sort field '{}' is not allowed, using default: '{}'",
                    result.sortField(), allowedSortFields.getFirst());
            result = result.withSort(allowedSortFields.getFirst(), result.sortDirection());
        }

        return result;
    }

    private static Map<String, Object> sanitizeFilters(
            Map<String, Object> filters,
            List<String> allowedFilterFields
    ) {
        if (filters == null || filters.isEmpty()) {
            return new HashMap<>();
        }

        Map<String, Object> sanitized = new HashMap<>();
        boolean hasAllowList = !allowedFilterFields.isEmpty();

        for (Map.Entry<?, ?> entry : filters.entrySet()) {
            if (entry.getKey() == null) {
                continue;
            }

            String fieldName = normalizeFieldName(entry.getKey());

            if (!hasAllowList || allowedFilterFields.contains(fieldName)) {
                sanitized.put(fieldName, entry.getValue());
            } else {
                log.warn("Filter field '{}' is not in the allowed list and will be ignored", fieldName);
            }
        }

        return sanitized;
    }

    private static String normalizeFieldName(Object key) {
        return (key instanceof Enum<?> e) ? e.name() : key.toString();
    }

    private static <E extends Enum<E>> List<String> extractEnumNames(Class<E> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}