package dev.ngb.base_hub.system_admin.organization.application.command;

import dev.ngb.base_hub.base.command.Command;
import dev.ngb.base_hub.util.StringUtils;

public record CreateOrganizationCommand(
        String name,
        String code,
        String domain,
        String contact,
        String description,
        String adminName,
        String adminEmail
) implements Command<Void> {
    public CreateOrganizationCommand {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        if (StringUtils.isBlank(code)) {
            throw new IllegalArgumentException("Code cannot be null or blank");
        }
        if (StringUtils.isBlank(domain)) {
            throw new IllegalArgumentException("Domain cannot be null or blank");
        }
        if (StringUtils.isBlank(contact)) {
            throw new IllegalArgumentException("Contact cannot be null or blank");
        }
        if (StringUtils.isBlank(adminName)) {
            throw new IllegalArgumentException("Admin name cannot be null or blank");
        }
        if (!StringUtils.isEmail(adminEmail)) {
            throw new IllegalArgumentException("Invalid admin email");
        }
    }
}
