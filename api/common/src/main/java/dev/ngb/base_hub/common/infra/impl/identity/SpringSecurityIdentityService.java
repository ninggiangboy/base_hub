package dev.ngb.base_hub.common.infra.impl.identity;

import dev.ngb.base_hub.base.annotation.InfraService;
import dev.ngb.base_hub.common.api.identity.CurrentUser;
import dev.ngb.base_hub.common.api.identity.IdentityService;

@InfraService
public class SpringSecurityIdentityService implements IdentityService {
    @Override
    public String getCurrentUserId() {
        return "";
    }

    @Override
    public CurrentUser getCurrentUser() {
        return null;
    }
}
