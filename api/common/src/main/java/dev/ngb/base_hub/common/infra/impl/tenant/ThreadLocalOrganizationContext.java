package dev.ngb.base_hub.common.infra.impl.tenant;

import dev.ngb.base_hub.common.api.tenant.OrganizationContextHolder;
import dev.ngb.base_hub.common.base.annotation.InfraService;

@InfraService
public class ThreadLocalOrganizationContext implements OrganizationContextHolder {

    private static final ThreadLocal<String> CURRENT_ORG = new ThreadLocal<>();

    @Override
    public void setCurrentOrgId(String tenantId) {
        if (tenantId == null) {
            CURRENT_ORG.remove();
        } else {
            CURRENT_ORG.set(tenantId);
        }
    }

    @Override
    public String getCurrentOrgId() {
        return CURRENT_ORG.get();
    }

    @Override
    public void clear() {
        CURRENT_ORG.remove();
    }
}
