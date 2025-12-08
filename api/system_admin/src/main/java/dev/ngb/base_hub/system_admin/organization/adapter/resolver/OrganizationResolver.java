package dev.ngb.base_hub.system_admin.organization.adapter.resolver;

import dev.ngb.base_hub.system_admin.organization.application.command.CreateOrganizationCommand;
import dev.ngb.base_hub.base.command.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class OrganizationResolver {

    private final CommandHandler<CreateOrganizationCommand, Void> createOrganizationCommandHandler;

    @MutationMapping()
    public void createOrganization() {
        CreateOrganizationCommand command = new CreateOrganizationCommand(
                "Organization Name",
                UUID.randomUUID().toString(),
                "org-domain.com",
                "123-456-7890",
                "Organization Description",
                "Admin Name",
                "admin@mail.com");
        createOrganizationCommandHandler.execute(command);
    }

}
