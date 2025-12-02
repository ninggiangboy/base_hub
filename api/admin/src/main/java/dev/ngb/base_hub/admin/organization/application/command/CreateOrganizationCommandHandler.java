package dev.ngb.base_hub.admin.organization.application.command;

import dev.ngb.base_hub.admin.shared.organization.event.OrganizationCreatedEvent;
import dev.ngb.base_hub.common.api.event.EventPublisher;
import dev.ngb.base_hub.common.base.annotation.UseCaseService;
import dev.ngb.base_hub.common.base.command.CommandHandler;
import dev.ngb.base_hub.common.base.domain.Result;
import dev.ngb.base_hub.common.base.event.IntegrationEvent;
import dev.ngb.base_hub.common.domain.organization.error.TenantError;
import dev.ngb.base_hub.common.domain.organization.model.Organization;
import dev.ngb.base_hub.common.domain.organization.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UseCaseService
@RequiredArgsConstructor
public class CreateOrganizationCommandHandler implements CommandHandler<CreateOrganizationCommand, Void> {

    private final OrganizationRepository organizationRepository;
    private final EventPublisher eventPublisher;

    @Override
    public Result<Void> execute(CreateOrganizationCommand command) {
        log.info("Executing CreateOrganizationCommand: {}", command);
        // find by code to prevent duplicate organization codes
        if (organizationRepository.findByCode(command.code()).isPresent()) {
            return Result.failure(TenantError.DUPLICATE_TENANT_CODE);
        }

        Organization organization = Organization.create(
                command.name(),
                command.code(),
                command.domain(),
                command.contact(),
                command.description()
        );
        organization = organizationRepository.create(organization);

        IntegrationEvent tenantCreatedEvent = new OrganizationCreatedEvent(
                organization.getId().toString(),
                command.adminName(),
                command.adminEmail()
        );
        eventPublisher.publish(tenantCreatedEvent);
        return Result.success();
    }

}
