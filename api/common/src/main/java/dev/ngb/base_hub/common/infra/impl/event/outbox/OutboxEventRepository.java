package dev.ngb.base_hub.common.infra.impl.event.outbox;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface OutboxEventRepository extends CrudRepository<OutboxEventEntity, UUID> {
}
