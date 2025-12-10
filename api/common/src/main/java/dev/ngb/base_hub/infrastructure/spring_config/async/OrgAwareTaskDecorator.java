package dev.ngb.base_hub.infrastructure.spring_config.async;

import dev.ngb.base_hub.application.spi.tenant.TenantContextService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.core.task.TaskDecorator;

@RequiredArgsConstructor
public class OrgAwareTaskDecorator implements TaskDecorator {

    private final TenantContextService tenantContextService;

    @Override
    @NonNull
    public Runnable decorate(@NonNull Runnable runnable) {
        String tenantId = tenantContextService.getTenantId();
        return () -> {
            try {
                tenantContextService.setTenantId(tenantId);
                runnable.run();
            } finally {
                tenantContextService.clear();
            }
        };
    }
}
