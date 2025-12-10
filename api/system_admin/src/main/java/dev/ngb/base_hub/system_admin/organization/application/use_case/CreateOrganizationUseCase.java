package dev.ngb.base_hub.system_admin.organization.application.use_case;

import dev.ngb.base_hub.application.spi.migration.MigrationService;
import dev.ngb.base_hub.system_admin.organization.application.command.CreateOrganizationCommand;
import dev.ngb.base_hub.domain.base.BusinessException;
import dev.ngb.base_hub.common.annotation.UseCaseService;
import dev.ngb.base_hub.application.use_case.UseCaseHandler;
import dev.ngb.base_hub.domain.organization.error.OrganizationError;
import dev.ngb.base_hub.domain.organization.model.Organization;
import dev.ngb.base_hub.domain.organization.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;

@Slf4j
@UseCaseService
@RequiredArgsConstructor
public class CreateOrganizationUseCase implements UseCaseHandler<CreateOrganizationCommand, Void> {

    private final OrganizationRepository organizationRepository;
    //    private final UserPublicApi userPublicApi;
    private final MigrationService migrationService;

    @Override
    public Void execute(CreateOrganizationCommand command) {
        // find by code to prevent duplicate organization codes
        Optional<Organization> existedOrgByCode = organizationRepository.findByCode(command.code());
        if (existedOrgByCode.isPresent()) {
            Map<String, Object> errorData = Map.of("existedOrgId", existedOrgByCode.get().getId());
            throw new BusinessException(OrganizationError.DUPLICATE_ORG_CODE, errorData);
        }

        Organization organization = Organization.create(
                command.name(),
                command.code(),
                command.domain(),
                command.contact(),
                command.description()
        );
        organization = organizationRepository.create(organization);

        // thinking about moving to event driven
        migrationService.performSchemaMigration(organization.getId().toString());
//        userPublicApi.createDefaultAdminForOrganization(
//                command.adminName(),
//                command.adminEmail()
//        );
        return null;
    }

}
