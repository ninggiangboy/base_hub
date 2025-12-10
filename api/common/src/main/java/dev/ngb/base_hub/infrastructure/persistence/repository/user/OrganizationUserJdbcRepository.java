package dev.ngb.base_hub.infrastructure.persistence.repository.user;

import dev.ngb.base_hub.infrastructure.persistence.entity.user.OrganizationUserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface OrganizationUserJdbcRepository extends CrudRepository<OrganizationUserEntity, UUID> {
}
