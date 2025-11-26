package dev.ngb.base_hub.common.base.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Setter
@Getter
public abstract class DomainEntity<T> {
    protected T id;
    protected UUID createdById;
    protected UUID updatedById;
    protected Instant createdAt;
    protected Instant updatedAt;

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
}
