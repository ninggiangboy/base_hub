package dev.ngb.base_hub.common.infra.impl;

import dev.ngb.base_hub.base.annotation.InfraService;
import dev.ngb.base_hub.common.api.identity.IdentityService;

@InfraService
public class SpringSecurityIdentityService implements IdentityService {
    @Override
    public String getCurrentUserId() {
        return "";
    }
}
