package dev.ngb.base_hub.common.infra.impl.tenant;

import dev.ngb.base_hub.common.api.tenant.TenantContextHolder;
import dev.ngb.base_hub.common.base.annotation.InfraService;

@InfraService
public class ThreadLocalTenantContext implements TenantContextHolder {

    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();

    @Override
    public void setCurrentTenantId(String tenantId) {
        if (tenantId == null) {
            CURRENT_TENANT.remove();
        } else {
            CURRENT_TENANT.set(tenantId);
        }
    }

    @Override
    public String getCurrentTenantId() {
        return CURRENT_TENANT.get();
    }

    @Override
    public void clear() {
        CURRENT_TENANT.remove();
    }
}
