package dev.ngb.base_hub.domain.organization.model;

import dev.ngb.base_hub.domain.base.BaseDomainEntity;
import dev.ngb.base_hub.domain.base.BusinessException;
import dev.ngb.base_hub.domain.organization.constant.OrganizationStatus;
import dev.ngb.base_hub.domain.organization.error.OrganizationError;
import lombok.Getter;

import java.time.Instant;
import java.util.Map;

import static dev.ngb.base_hub.common.util.StringUtils.isBlank;

@Getter
public class Organization extends BaseDomainEntity<Long> {
    private String name;
    private String code;
    private String domain;
    private String contact;
    private String description;
    private OrganizationStatus status;
    private Map<String, Object> configuration;

    private Organization() {
    }

    public static Organization reconstruct(
            Long id,
            String name,
            String code,
            String domain,
            String contact,
            String description,
            OrganizationStatus status,
            Map<String, Object> configuration,
            Integer version,
            String createdById,
            String updatedById,
            Instant createdAt,
            Instant updatedAt) {
        Organization organization = new Organization();
        organization.id = id;
        organization.name = name;
        organization.code = code.toUpperCase();
        organization.domain = domain;
        organization.contact = contact;
        organization.description = description;
        organization.status = status;
        organization.configuration = configuration;
        organization.version = version;
        organization.createdById = createdById;
        organization.updatedById = updatedById;
        organization.createdAt = createdAt;
        organization.updatedAt = updatedAt;
        return organization;
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
        organization.status = OrganizationStatus.INITIALIZING;
        organization.checkState();
        return organization;
    }

    public void active() {
        this.status = OrganizationStatus.ACTIVE;
    }

    public void inactive() {
        this.status = OrganizationStatus.INACTIVE;
    }

    public void update(String name,
                       String domain,
                       String contact,
                       String description,
                       Map<String, Object> configuration) {
        this.name = name;
        this.domain = domain;
        this.contact = contact;
        this.description = description;
        this.configuration = configuration;
        this.checkState();
    }

    private void checkState() {
        if (isBlank(name) || isBlank(code) || isBlank(domain) || isBlank(contact)) {
            throw new BusinessException(OrganizationError.INVALID_ORG_DATA);
        }
    }
}
