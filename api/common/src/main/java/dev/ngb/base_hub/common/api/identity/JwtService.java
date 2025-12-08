package dev.ngb.base_hub.common.api.identity;

import java.util.Map;

public interface JwtService {
    String generateToken(Long userId, Map<String, Object> claims, Integer ttlMinutes);
}
