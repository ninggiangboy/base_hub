package dev.ngb.base_hub.infrastructure.persistence.repository;

import dev.ngb.base_hub.infrastructure.persistence.entity.OutboxEventEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface OutboxEventJdbcRepository extends CrudRepository<OutboxEventEntity, UUID> {
}
