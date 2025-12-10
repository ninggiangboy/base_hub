package dev.ngb.base_hub.application.spi.security;

import java.util.Map;

public interface JwtService {
    String generateToken(Long userId, Map<String, Object> claims, Integer ttlMinutes);
}
