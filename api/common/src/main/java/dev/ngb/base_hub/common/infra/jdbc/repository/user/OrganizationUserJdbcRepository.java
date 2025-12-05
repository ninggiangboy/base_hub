package dev.ngb.base_hub.common.infra.jdbc.repository.user;

import dev.ngb.base_hub.common.infra.jdbc.entity.user.OrganizationUserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface OrganizationUserJdbcRepository extends CrudRepository<OrganizationUserEntity, UUID> {
}
