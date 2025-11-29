package dev.ngb.base_hub.common.api.tenant;

public interface OrganizationContextHolder {
    void setCurrentOrgId(String tenantId);

    String getCurrentOrgId();

    void clear();
}
