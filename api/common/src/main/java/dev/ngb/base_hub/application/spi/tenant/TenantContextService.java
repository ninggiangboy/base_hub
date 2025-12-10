package dev.ngb.base_hub.application.spi.tenant;

import org.jspecify.annotations.Nullable;

public interface TenantContextService {
    void setTenantId(@Nullable String tenantId);

    @Nullable
    String getTenantId();

    void clear();
}
