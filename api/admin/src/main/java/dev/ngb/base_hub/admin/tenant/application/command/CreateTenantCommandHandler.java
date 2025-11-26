package dev.ngb.base_hub.admin.tenant.application.command;

import dev.ngb.base_hub.admin.shared.tenant.event.TenantCreatedEvent;
import dev.ngb.base_hub.common.api.event.EventPublisher;
import dev.ngb.base_hub.admin.tenant.application.spi.MigrationService;
import dev.ngb.base_hub.common.base.annotation.UseCaseService;
import dev.ngb.base_hub.common.base.command.CommandHandler;
import dev.ngb.base_hub.common.base.domain.Result;
import dev.ngb.base_hub.common.base.event.IntegrationEvent;
import dev.ngb.base_hub.common.domain.tenant.error.TenantError;
import dev.ngb.base_hub.common.domain.tenant.model.Tenant;
import dev.ngb.base_hub.common.domain.tenant.repository.TenantRepository;
import lombok.RequiredArgsConstructor;

@UseCaseService
@RequiredArgsConstructor
public class CreateTenantCommandHandler implements CommandHandler<CreateTenantCommand, Void> {

    private final TenantRepository tenantRepository;
    private final EventPublisher eventPublisher;
    private final MigrationService migrationService;

    @Override
    public Result<Void> execute(CreateTenantCommand command) {
        // find by code to prevent duplicate tenant codes
        if (tenantRepository.findByCode(command.code()).isPresent()) {
            return Result.failure(TenantError.DUPLICATE_TENANT_CODE);
        }

        Tenant tenant = Tenant.create(
                command.name(),
                command.code(),
                command.domain(),
                command.contact(),
                command.description()
        );
        tenant = tenantRepository.create(tenant);

        migrationService.performTenantMigration(tenant.getId().toString());

        IntegrationEvent tenantCreatedEvent = new TenantCreatedEvent(
                tenant.getId().toString(),
                command.adminName(),
                command.adminEmail()
        );
        eventPublisher.publish(tenantCreatedEvent);

        return Result.success();
    }

}
