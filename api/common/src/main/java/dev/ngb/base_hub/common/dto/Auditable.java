package dev.ngb.base_hub.common.dto;

import java.time.Instant;

public interface Auditable {
    String createdById();

    String updatedById();

    Instant createdAt();

    Instant updatedAt();
}

