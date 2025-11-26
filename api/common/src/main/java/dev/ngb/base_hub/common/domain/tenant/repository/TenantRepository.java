package dev.ngb.base_hub.common.domain.tenant.repository;

import dev.ngb.base_hub.common.base.domain.BaseDomainRepository;
import dev.ngb.base_hub.common.domain.tenant.model.Tenant;

import java.util.Optional;

public interface TenantRepository extends BaseDomainRepository<Tenant, Long> {
    Optional<Tenant> findByCode(String code);
}
