package dev.ngb.base_hub.admin.organization.application.event_handler;

import dev.ngb.base_hub.common.api.migration.MigrationService;
import dev.ngb.base_hub.admin.shared.organization.event.OrganizationCreatedEvent;
import dev.ngb.base_hub.common.api.tenant.OrganizationContextHolder;
import dev.ngb.base_hub.common.base.annotation.EventHandleService;
import dev.ngb.base_hub.common.base.event.IntegrationEventHandler;
import dev.ngb.base_hub.common.domain.organization.model.OrganizationUser;
import dev.ngb.base_hub.common.domain.organization.repository.OrganizationUserRepository;
import lombok.RequiredArgsConstructor;

@EventHandleService
@RequiredArgsConstructor
public class OrganizationCreatedEventHandler implements IntegrationEventHandler<OrganizationCreatedEvent> {

    private final OrganizationUserRepository organizationUserRepository;
    private final OrganizationContextHolder organizationContextHolder;
    private final MigrationService migrationService;

    @Override
    public void handle(OrganizationCreatedEvent event) {
        migrationService.performOrgMigration(event.orgId());
        // Set the tenant context to ensure following operations run under this tenant
        organizationContextHolder.setCurrentOrgId(event.orgId());
        // Create default admin user for the new tenant
        OrganizationUser admin = OrganizationUser.createAdmin(
                event.adminName(),
                event.adminEmail()
        );
        organizationUserRepository.create(admin);
        organizationContextHolder.clear();
    }
}
