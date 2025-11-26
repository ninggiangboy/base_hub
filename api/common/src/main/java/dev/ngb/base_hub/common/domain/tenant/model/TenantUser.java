package dev.ngb.base_hub.common.domain.tenant.model;

import dev.ngb.base_hub.common.base.domain.DomainEntity;
import dev.ngb.base_hub.common.domain.constant.TenantUserRole;
import dev.ngb.base_hub.common.domain.constant.TenantUserStatus;

import java.util.UUID;

public class TenantUser extends DomainEntity<UUID> {
    private String name;
    private String email;
    private String avatarUrl;
    private String hashedPassword;
    private TenantUserStatus status;
    private TenantUserRole role;

    private TenantUser() {
    }

    public static TenantUser createAdmin(String name, String email) {
        TenantUser tenantUser = new TenantUser();
        tenantUser.name = name;
        tenantUser.email = email;
        tenantUser.status = TenantUserStatus.INITIAL;
        tenantUser.role = TenantUserRole.ADMIN;
        return tenantUser;
    }

    public static TenantUser createUser(String name, String email) {
        TenantUser tenantUser = new TenantUser();
        tenantUser.name = name;
        tenantUser.email = email;
        tenantUser.status = TenantUserStatus.INITIAL;
        tenantUser.role = TenantUserRole.USER;
        return tenantUser;
    }
}
