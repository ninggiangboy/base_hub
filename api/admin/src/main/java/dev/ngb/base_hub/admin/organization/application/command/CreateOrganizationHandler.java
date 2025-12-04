package dev.ngb.base_hub.admin.organization.application.command;

import dev.ngb.base_hub.admin.shared.organization.event.OrganizationCreatedEvent;
import dev.ngb.base_hub.base.result.BusinessException;
import dev.ngb.base_hub.common.api.event.EventPublisher;
import dev.ngb.base_hub.base.annotation.UseCaseService;
import dev.ngb.base_hub.base.command.CommandHandler;
import dev.ngb.base_hub.base.event.IntegrationEvent;
import dev.ngb.base_hub.domain.organization.error.OrganizationError;
import dev.ngb.base_hub.domain.organization.model.Organization;
import dev.ngb.base_hub.domain.organization.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UseCaseService
@RequiredArgsConstructor
public class CreateOrganizationHandler implements CommandHandler<CreateOrganizationCommand, Void> {

    private final OrganizationRepository organizationRepository;
    private final EventPublisher eventPublisher;

    @Override
    public Void execute(CreateOrganizationCommand command) {
        // find by code to prevent duplicate organization codes
        if (organizationRepository.findByCode(command.code()).isPresent()) {
            throw new BusinessException(OrganizationError.DUPLICATE_CODE);
        }

        Organization organization = Organization.create(
                command.name(),
                command.code(),
                command.domain(),
                command.contact(),
                command.description()
        );
        organization = organizationRepository.create(organization);

        IntegrationEvent orgCreatedEvent = new OrganizationCreatedEvent(
                organization.getId().toString(),
                command.adminName(),
                command.adminEmail()
        );
        eventPublisher.publish(orgCreatedEvent);
        return null;
    }

}
