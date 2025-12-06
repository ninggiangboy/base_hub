package dev.ngb.base_hub.common.context;

public class OrganizationContextHolder {

    private static final ThreadLocal<String> CURRENT_ORG_ID = new ThreadLocal<>();

    public static void setCurrentOrgId(String orgId) {
        if (orgId == null) {
            CURRENT_ORG_ID.remove();
        } else {
            CURRENT_ORG_ID.set(orgId);
        }
    }

    public static String getCurrentOrgId() {
        return CURRENT_ORG_ID.get();
    }

    public static void clear() {
        CURRENT_ORG_ID.remove();
    }
}
