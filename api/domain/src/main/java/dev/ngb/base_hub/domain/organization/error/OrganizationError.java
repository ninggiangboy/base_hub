package dev.ngb.base_hub.domain.organization.error;

import dev.ngb.base_hub.base.domain.DomainError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrganizationError implements DomainError {
    DUPLICATE_CODE("Organization code already exists"),
    VERSION_CONFLICT("Organization has new update");
    private final String message;
}
