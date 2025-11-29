package dev.ngb.base_hub.common.domain.organization.model;

import dev.ngb.base_hub.common.base.domain.DomainEntity;
import dev.ngb.base_hub.common.domain.constant.OrganizationStatus;

public class Organization extends DomainEntity<Long> {
    private String name;
    private String code;
    private String domain;
    private String contact;
    private String description;
    private OrganizationStatus status;

    private Organization() {
    }

    public static Organization create(
            String name,
            String code,
            String domain,
            String contact,
            String description) {
        Organization organization = new Organization();
        organization.name = name;
        organization.code = code;
        organization.domain = domain;
        organization.contact = contact;
        organization.description = description;
        organization.status = OrganizationStatus.ACTIVE;
        return organization;
    }
}
