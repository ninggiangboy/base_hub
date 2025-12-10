package dev.ngb.base_hub.infrastructure.persistence.repository.organization;

import dev.ngb.base_hub.infrastructure.persistence.entity.organization.OrganizationEntity;
import org.springframework.data.repository.ListCrudRepository;


public interface OrganizationJdbcRepository extends ListCrudRepository<OrganizationEntity, Long> {
}
