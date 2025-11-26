package dev.ngb.base_hub.common.api.tenant;

public interface TenantContextHolder {
    void setCurrentTenantId(String tenantId);

    String getCurrentTenantId();

    void clear();
}
