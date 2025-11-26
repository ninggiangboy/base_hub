package dev.ngb.base_hub.common.domain.tenant.error;

import dev.ngb.base_hub.common.base.domain.DomainError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TenantError implements DomainError {
    DUPLICATE_TENANT_CODE("Tenant code already exists");
    private final String message;
}
