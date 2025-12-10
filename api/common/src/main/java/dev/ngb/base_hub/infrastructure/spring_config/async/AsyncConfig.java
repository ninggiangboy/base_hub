package dev.ngb.base_hub.infrastructure.spring_config.async;

import dev.ngb.base_hub.application.spi.tenant.TenantContextService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

@Configuration
public class AsyncConfig implements AsyncConfigurer {

    @Bean
    @Primary
    public AsyncTaskExecutor orgAwareExecutor(TenantContextService tenantContextService) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(8);
        executor.setMaxPoolSize(16);
        executor.setQueueCapacity(100);
        executor.setTaskDecorator(new OrgAwareTaskDecorator(tenantContextService));
        executor.initialize();
        return new DelegatingSecurityContextAsyncTaskExecutor(executor);
    }
}
