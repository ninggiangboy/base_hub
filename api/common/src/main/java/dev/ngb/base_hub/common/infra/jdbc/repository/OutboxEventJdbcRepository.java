package dev.ngb.base_hub.common.infra.jdbc.repository;

import dev.ngb.base_hub.common.infra.jdbc.entity.OutboxEventEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface OutboxEventJdbcRepository extends CrudRepository<OutboxEventEntity, UUID> {
}
