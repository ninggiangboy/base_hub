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
        String orgId = OrganizationContextHolder.getCurrentOrgId();
        return () -> {
            try {
                OrganizationContextHolder.setCurrentOrgId(orgId);
                runnable.run();
            } finally {
                OrganizationContextHolder.clear();
            }
        };
    }
}
