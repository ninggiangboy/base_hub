package dev.ngb.base_hub.common.domain.organization.model;

import dev.ngb.base_hub.common.domain.base.BaseUser;
import dev.ngb.base_hub.common.domain.constant.OrganizationUserRole;
import dev.ngb.base_hub.common.domain.constant.OrganizationUserStatus;

public class OrganizationUser extends BaseUser {
    private OrganizationUserStatus status;
    private OrganizationUserRole role;

    private OrganizationUser() {
    }

    public static OrganizationUser createAdmin(String name, String email) {
        OrganizationUser organizationUser = new OrganizationUser();
        organizationUser.email = email;
        organizationUser.status = OrganizationUserStatus.INITIAL;
        organizationUser.role = OrganizationUserRole.ADMIN;
        organizationUser.initializeBaseUser(email, email, name);
        return organizationUser;
    }

    public static OrganizationUser createUser(String name, String email) {
        OrganizationUser organizationUser = new OrganizationUser();
        organizationUser.email = email;
        organizationUser.status = OrganizationUserStatus.INITIAL;
        organizationUser.role = OrganizationUserRole.USER;
        organizationUser.initializeBaseUser(email, email, name);
        return organizationUser;
    }

    @Override
    public void resetPassword(String newHashedPassword, String credentialToken) {
        super.resetPassword(newHashedPassword, credentialToken);
        if (this.status == OrganizationUserStatus.INITIAL) {
            this.status = OrganizationUserStatus.ACTIVE;
        }
    }
}
