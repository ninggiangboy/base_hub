package dev.ngb.base_hub.admin.organization.application.command;

import dev.ngb.base_hub.base.command.Command;
import dev.ngb.base_hub.base.util.StringUtils;

public record OrganizationUpdateCommand(
        String name,
        String domain,
        String contact,
        String description
) implements Command<Void> {
    public OrganizationUpdateCommand {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        if (StringUtils.isBlank(domain)) {
            throw new IllegalArgumentException("Domain cannot be null or blank");
        }
        if (StringUtils.isBlank(contact)) {
            throw new IllegalArgumentException("Contact cannot be null or blank");
        }
    }
}
