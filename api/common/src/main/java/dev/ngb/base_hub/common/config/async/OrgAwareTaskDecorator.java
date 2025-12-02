package dev.ngb.base_hub.common.config.async;

import dev.ngb.base_hub.common.context.OrganizationContextHolder;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.core.task.TaskDecorator;

@RequiredArgsConstructor
public class OrgAwareTaskDecorator implements TaskDecorator {

    @Override
    @NonNull
    public Runnable decorate(@NonNull Runnable runnable) {
        String tenantId = OrganizationContextHolder.getCurrentOrgId();
        return () -> {
            try {
                OrganizationContextHolder.setCurrentOrgId(tenantId);
                runnable.run();
            } finally {
                OrganizationContextHolder.clear();
            }
        };
    }
}
