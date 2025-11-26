package dev.ngb.base_hub.common.domain.tenant.repository;

import dev.ngb.base_hub.common.base.domain.BaseDomainRepository;
import dev.ngb.base_hub.common.domain.tenant.model.TenantUser;

import java.util.UUID;

public interface TenantUserRepository extends BaseDomainRepository<TenantUser, UUID> {
}
