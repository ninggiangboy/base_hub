package dev.ngb.base_hub.admin.shared.notification.public_api;

import dev.ngb.base_hub.common.domain.base.BaseUser;

public interface NotificationPublicApi {
    void sendWelcomeEmailForUserOrg(BaseUser admin);
}