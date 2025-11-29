package dev.ngb.base_hub.common.config.async;

import dev.ngb.base_hub.common.api.tenant.OrganizationContextHolder;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.core.task.TaskDecorator;

@RequiredArgsConstructor
public class OrgAwareTaskDecorator implements TaskDecorator {

    private final OrganizationContextHolder organizationContextHolder;

    @Override
    @NonNull
    public Runnable decorate(@NonNull Runnable runnable) {
        String tenantId = organizationContextHolder.getCurrentOrgId();

        return () -> {
            try {
                organizationContextHolder.setCurrentOrgId(tenantId);
                runnable.run();
            } finally {
                organizationContextHolder.clear();
            }
        };
    }
}
