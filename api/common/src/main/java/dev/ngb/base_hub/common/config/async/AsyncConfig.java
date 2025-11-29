package dev.ngb.base_hub.common.config.async;

import dev.ngb.base_hub.common.api.tenant.OrganizationContextHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AsyncConfig implements AsyncConfigurer {

    @Bean(name = "orgAwareExecutor")
    public ThreadPoolTaskExecutor tenantAwareExecutor(OrganizationContextHolder organizationContextHolder) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(8);
        executor.setMaxPoolSize(16);
        executor.setQueueCapacity(100);
        executor.setTaskDecorator(new OrgAwareTaskDecorator(organizationContextHolder));
        executor.initialize();
        return executor;
    }
}
