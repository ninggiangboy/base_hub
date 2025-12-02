package dev.ngb.base_hub.domain.user.repository;

import dev.ngb.base_hub.base.domain.BaseDomainRepository;
import dev.ngb.base_hub.domain.user.model.OrganizationUser;

import java.util.UUID;

public interface OrganizationUserRepository extends BaseDomainRepository<OrganizationUser, UUID> {
}
