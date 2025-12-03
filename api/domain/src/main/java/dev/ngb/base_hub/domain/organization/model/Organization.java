package dev.ngb.base_hub.domain.organization.model;

import dev.ngb.base_hub.base.domain.DomainEntity;
import dev.ngb.base_hub.domain.constant.OrganizationStatus;
import lombok.Getter;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Getter
public class Organization extends DomainEntity<Long> {
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
            UUID createdById,
            UUID updatedById,
            Instant createdAt,
            Instant updatedAt) {
        Organization organization = new Organization();
        organization.id = id;
        organization.name = name;
        organization.code = code;
        organization.domain = domain;
        organization.contact = contact;
        organization.description = description;
        organization.status = status;
        organization.configuration = configuration;
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
        return organization;
    }
}
