package dev.ngb.base_hub.common.domain.organization.repository;

import dev.ngb.base_hub.common.base.domain.BaseDomainRepository;
import dev.ngb.base_hub.common.domain.organization.model.OrganizationUser;

import java.util.UUID;

public interface OrganizationUserRepository extends BaseDomainRepository<OrganizationUser, UUID> {
}
