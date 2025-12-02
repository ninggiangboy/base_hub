package dev.ngb.base_hub.common.context;

public class OrganizationContextHolder {

    private static final ThreadLocal<String> CURRENT_ORG = new ThreadLocal<>();

    public static void setCurrentOrgId(String orgId) {
        if (orgId == null) {
            CURRENT_ORG.remove();
        } else {
            CURRENT_ORG.set(orgId);
        }
    }

    public static String getCurrentOrgId() {
        return CURRENT_ORG.get();
    }

    public static void clear() {
        CURRENT_ORG.remove();
    }
}
