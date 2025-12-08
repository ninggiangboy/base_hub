package dev.ngb.base_hub.util;

import com.github.f4b6a3.uuid.UuidCreator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UUIDUtils {
    public static UUID generateV7() {
        return UuidCreator.getTimeOrderedEpoch();
    }
}
