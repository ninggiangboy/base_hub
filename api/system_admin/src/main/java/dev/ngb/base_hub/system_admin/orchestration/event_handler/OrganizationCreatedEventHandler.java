package dev.ngb.base_hub.system_admin.orchestration.event_handler;

import dev.ngb.base_hub.system_admin.shared.migration.public_api.MigrationPublicApi;
import dev.ngb.base_hub.system_admin.shared.organization.event.OrganizationCreatedEvent;
import dev.ngb.base_hub.common.context.OrganizationContextHolder;
import dev.ngb.base_hub.base.annotation.EventHandlerService;
import dev.ngb.base_hub.base.event.EventHandler;
import lombok.RequiredArgsConstructor;

@EventHandlerService
@RequiredArgsConstructor
public class OrganizationCreatedEventHandler implements EventHandler<OrganizationCreatedEvent> {

    //    private final UserPublicApi userPublicApi;
//    private final NotificationPublicApi notificationPublicApi;
//    private final OrganizationPublicApi organizationPublicApi;
    private final MigrationPublicApi migrationPublicApi;

    @Override
    public void handle(OrganizationCreatedEvent event) {
        try {
            migrationPublicApi.performOrgSchemaMigration(event.orgId());
            OrganizationContextHolder.setCurrentOrgId(event.orgId());
//            BaseUser admin = userPublicApi.createDefaultAdminForOrganization(event.adminName(), event.adminEmail());
//            organizationPublicApi.completedInitOrganization();
//            notificationPublicApi.sendWelcomeEmailForUserOrg(admin);
        } catch (RuntimeException ex) {
//            organizationPublicApi.failedInitOrganization();
            throw ex;
        } finally {
            OrganizationContextHolder.clear();
        }
    }
}
