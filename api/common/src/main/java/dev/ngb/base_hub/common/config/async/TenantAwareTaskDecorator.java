package dev.ngb.base_hub.common.config.async;

import dev.ngb.base_hub.common.api.tenant.TenantContextHolder;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.core.task.TaskDecorator;

@RequiredArgsConstructor
public class TenantAwareTaskDecorator implements TaskDecorator {

    private final TenantContextHolder tenantContextHolder;

    @Override
    @NonNull
    public Runnable decorate(@NonNull Runnable runnable) {
        String tenantId = tenantContextHolder.getCurrentTenantId();

        return () -> {
            try {
                tenantContextHolder.setCurrentTenantId(tenantId);
                runnable.run();
            } finally {
                tenantContextHolder.clear();
            }
        };
    }
}
