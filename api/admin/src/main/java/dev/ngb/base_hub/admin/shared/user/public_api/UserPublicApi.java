package dev.ngb.base_hub.admin.shared.user.public_api;

import dev.ngb.base_hub.common.domain.user.model.OrganizationUser;

public interface UserPublicApi {
    OrganizationUser createDefaultAdminForOrganization(String adminName, String adminEmail);
}
