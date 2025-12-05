package dev.ngb.base_hub.base.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
public abstract class DomainEntity<T> {
    protected T id;
    protected Long version;
    protected String createdById;
    protected String updatedById;
    protected String deletedById;
    protected Instant createdAt;
    protected Instant updatedAt;
    protected Instant deletedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DomainEntity<?> that = (DomainEntity<?>) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void markCreatedBy(String userId) {
        createdById = userId;
        createdAt = Instant.now();
    }

    public void markUpdatedBy(String userId) {
        updatedById = userId;
        updatedAt = Instant.now();
    }

    public void markDeletedBy(String userId) {
        deletedById = userId;
        deletedAt = Instant.now();
    }
}
