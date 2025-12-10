package dev.ngb.base_hub.domain.organization.repository;

import dev.ngb.base_hub.domain.base.BaseDomainRepository;
import dev.ngb.base_hub.domain.organization.model.Organization;

import java.util.Optional;

public interface OrganizationRepository extends BaseDomainRepository<Organization, Long> {
    Optional<Organization> findByCode(String code);
}
