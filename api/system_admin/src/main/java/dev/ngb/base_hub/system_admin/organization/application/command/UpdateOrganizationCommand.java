package dev.ngb.base_hub.system_admin.organization.application.command;

import dev.ngb.base_hub.application.use_case.Command;
import dev.ngb.base_hub.common.util.StringUtils;
import org.jspecify.annotations.NonNull;

public record UpdateOrganizationCommand(
        @NonNull Long orgId,
        String name,
        String domain,
        String contact,
        String description
) implements Command<Void> {
    public UpdateOrganizationCommand {
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
