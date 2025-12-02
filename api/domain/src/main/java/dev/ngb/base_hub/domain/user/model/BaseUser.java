package dev.ngb.base_hub.domain.user.model;

import dev.ngb.base_hub.base.domain.DomainEntity;
import dev.ngb.base_hub.base.util.StringUtils;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class BaseUser extends DomainEntity<UUID> {
    protected String loginId;
    protected String email;
    protected String displayName;
    protected String hashedPassword;
    protected String credentialToken;
    protected Instant lastLoginAt;

    protected void initializeBaseUser(String loginId, String email, String displayName) {
        this.loginId = loginId.toUpperCase();
        this.email = email;
        this.displayName = displayName;
        this.credentialToken = generateCredentialToken();
    }

    public String refreshCredentialToken() {
        this.credentialToken = generateCredentialToken();
        return this.credentialToken;
    }

    public void resetPassword(String newHashedPassword, String credentialToken) {
        if (!StringUtils.equals(this.credentialToken, credentialToken)) {
            throw new IllegalArgumentException("Invalid credential token");
        }
        this.hashedPassword = newHashedPassword;
        this.credentialToken = null;
    }

    protected String generateCredentialToken() {
        return UUID.randomUUID().toString();
    }
}