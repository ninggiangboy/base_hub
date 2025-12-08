package dev.ngb.base_hub.system_admin.organization.application.command;

import org.jspecify.annotations.NonNull;

public record DeleteOrganizationCommand(
        @NonNull Long orgId
) {
}
