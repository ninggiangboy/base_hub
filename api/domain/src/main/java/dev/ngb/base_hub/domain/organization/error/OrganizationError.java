package dev.ngb.base_hub.domain.organization.error;

import dev.ngb.base_hub.base.domain.DomainError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrganizationError implements DomainError {
    DUPLICATE_ORG_CODE("Organization code already exists"),
    ORG_DATA_UPDATED("Organization was updated by another user"),
    NOT_FOUND_ORG("Organization not found"),
    CAN_NOT_DELETE_ORG_IN_USE("Organization is in use and cannot be deleted");
    private final String message;
}
