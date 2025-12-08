package dev.ngb.base_hub.domain.user.model;

import dev.ngb.base_hub.domain.user.constant.OrganizationUserRole;
import dev.ngb.base_hub.domain.user.constant.OrganizationUserStatus;
import lombok.Getter;

@Getter
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
    public void setPassword(String newHashedPassword, String credentialToken) {
        super.setPassword(newHashedPassword, credentialToken);
        if (this.status == OrganizationUserStatus.INITIAL) {
            this.status = OrganizationUserStatus.ACTIVE;
        }
    }
}
