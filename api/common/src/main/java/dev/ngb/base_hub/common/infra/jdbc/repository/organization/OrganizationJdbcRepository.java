package dev.ngb.base_hub.common.infra.jdbc.repository.organization;

import dev.ngb.base_hub.common.infra.jdbc.entity.organization.OrganizationEntity;
import org.springframework.data.repository.ListCrudRepository;


public interface OrganizationJdbcRepository extends ListCrudRepository<OrganizationEntity, Long> {
}
