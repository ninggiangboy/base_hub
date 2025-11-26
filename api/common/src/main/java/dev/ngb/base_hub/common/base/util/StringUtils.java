package dev.ngb.base_hub.common.base.util;

import jakarta.xml.bind.DatatypeConverter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtils {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[\\w-.]+@[\\w-]+\\.[a-z]{2,}$",
            Pattern.CASE_INSENSITIVE
    );

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isEmail(String value) {
        if (isBlank(value)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(value).matches();
    }

    public static String toSnakeCase(String value) {
        return value
                .replaceAll("([a-z])([A-Z]+)", "$1_$2")
                .toLowerCase();
    }

    /**
     * Generate a stable, hash-based string map of parameters.
     * Uses TreeMap to sort keys and SHA-256 for collision-resistant hash.
     */
    public static String generateParamHash(Map<String, Object> params) {
        try {
            // Sort map to ensure consistent order
            Map<String, Object> sorted = new TreeMap<>(params);
            StringBuilder sb = new StringBuilder();
            sorted.forEach((k, v) -> sb.append(k).append("=").append(v).append("&"));

            // Compute SHA-256 hash
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(sb.toString().getBytes(StandardCharsets.UTF_8));

            // Convert hash to hex string
            return DatatypeConverter.printHexBinary(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to generate key", e);
        }
    }
}
