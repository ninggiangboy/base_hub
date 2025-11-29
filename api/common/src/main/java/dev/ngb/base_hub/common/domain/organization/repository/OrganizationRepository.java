package dev.ngb.base_hub.common.domain.organization.repository;

import dev.ngb.base_hub.common.base.domain.BaseDomainRepository;
import dev.ngb.base_hub.common.domain.organization.model.Organization;

import java.util.Optional;

public interface OrganizationRepository extends BaseDomainRepository<Organization, Long> {
    Optional<Organization> findByCode(String code);
}
