package dev.ngb.base_hub.common.context;

public class OrganizationContextHolder {

    private static final ThreadLocal<String> CURRENT_ORG = new ThreadLocal<>();

    public static void setCurrentOrgId(String tenantId) {
        if (tenantId == null) {
            CURRENT_ORG.remove();
        } else {
            CURRENT_ORG.set(tenantId);
        }
    }

    public static String getCurrentOrgId() {
        return CURRENT_ORG.get();
    }

    public static void clear() {
        CURRENT_ORG.remove();
    }
}
