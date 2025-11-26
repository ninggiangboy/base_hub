package dev.ngb.base_hub.admin.tenant.application.event_handler;

import dev.ngb.base_hub.admin.shared.tenant.event.TenantCreatedEvent;
import dev.ngb.base_hub.common.api.tenant.TenantContextHolder;
import dev.ngb.base_hub.common.base.annotation.EventHandleService;
import dev.ngb.base_hub.common.base.event.IntegrationEventHandler;
import dev.ngb.base_hub.common.domain.tenant.model.TenantUser;
import dev.ngb.base_hub.common.domain.tenant.repository.TenantUserRepository;
import lombok.RequiredArgsConstructor;

@EventHandleService
@RequiredArgsConstructor
public class TenantCreatedEventHandler implements IntegrationEventHandler<TenantCreatedEvent> {

    private final TenantUserRepository tenantUserRepository;
    private final TenantContextHolder tenantContextHolder;

    @Override
    public void handle(TenantCreatedEvent event) {
        // Set the tenant context to ensure following operations run under this tenant
        tenantContextHolder.setCurrentTenantId(event.tenantId());
        // Create default admin user for the new tenant
        TenantUser admin = TenantUser.createAdmin(
                event.adminName(),
                event.adminEmail()
        );
        tenantUserRepository.create(admin);
        tenantContextHolder.clear();
    }
}
