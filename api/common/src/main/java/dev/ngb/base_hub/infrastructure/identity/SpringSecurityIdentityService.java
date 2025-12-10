package dev.ngb.base_hub.infrastructure.identity;

import dev.ngb.base_hub.application.spi.security.CurrentUser;
import dev.ngb.base_hub.application.spi.security.IdentityService;
import org.springframework.stereotype.Component;

@Component
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
