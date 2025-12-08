package dev.ngb.base_hub.common.infra.impl.identity;

import dev.ngb.base_hub.common.api.identity.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.*;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Map;

@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private static final String SECRET = "my-secret-key-which-should-be-long-enough";
    private static final long EXPIRATION_MS = 3600_000;

    private final JwtDecoder jwtDecoder;
    private final JwtEncoder jwtEncoder;

    public JwtServiceImpl() {
        byte[] keyBytes = SECRET.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "HmacSHA256");
        this.jwtDecoder = NimbusJwtDecoder.withSecretKey(keySpec).build();
        this.jwtEncoder = NimbusJwtEncoder.withSecretKey(keySpec).build();
    }

    @Override
    public String generateToken(Long userId, Map<String, Object> claims, Integer ttlMinutes) {
        Instant now = Instant.now();
        JwtClaimsSet jwt = JwtClaimsSet.builder()
                .subject(String.valueOf(userId))
                .claims(map -> map.putAll(claims))
                .issuedAt(now)
                .expiresAt(now.plus(ttlMinutes, ChronoUnit.MINUTES))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(jwt)).getTokenValue();

    }

    private String HmacSHA256(String data, String secret) {
        try {
            javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
