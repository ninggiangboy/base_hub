package dev.ngb.base_hub.application.spi.security;

import dev.ngb.base_hub.common.constant.UserType;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public record CurrentUser(
        UUID userId,
        String loginId,
        UserType userType,
        @Nullable Long orgId,
        @Nullable Long appId,
        String accessToken
) {
}