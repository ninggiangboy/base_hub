package dev.ngb.base_hub.system_admin._shared.notification.public_api;

import dev.ngb.base_hub.domain.user.model.BaseUser;

public interface NotificationPublicApi {
    void sendWelcomeEmailForUserOrg(BaseUser admin);
}