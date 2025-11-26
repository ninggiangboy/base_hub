package dev.ngb.base_hub.common.domain.tenant.model;

import dev.ngb.base_hub.common.base.domain.DomainEntity;
import dev.ngb.base_hub.common.domain.constant.TenantStatus;

public class Tenant extends DomainEntity<Long> {
    private String name;
    private String code;
    private String domain;
    private String contact;
    private String description;
    private TenantStatus status;

    private Tenant() {
    }

    public static Tenant create(
            String name,
            String code,
            String domain,
            String contact,
            String description) {
        Tenant tenant = new Tenant();
        tenant.name = name;
        tenant.code = code;
        tenant.domain = domain;
        tenant.contact = contact;
        tenant.description = description;
        tenant.status = TenantStatus.ACTIVE;
        return tenant;
    }
}
