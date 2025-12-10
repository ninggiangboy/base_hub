package dev.ngb.base_hub.application.spi.security;

public interface IdentityService {
    String getCurrentUserId();

    CurrentUser getCurrentUser();
}

