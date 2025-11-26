package dev.ngb.base_hub.admin.user.adapter.consumber;

import dev.ngb.base_hub.admin.shared.tenant.event.TenantCreatedEvent;
import dev.ngb.base_hub.common.base.event.IntegrationEventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserConsumer {

    private final IntegrationEventHandler<TenantCreatedEvent> tenantCreatedEventHandler;

    @RabbitListener(queues = "events.user.queue")
    public void handleTenantCreatedEvent(TenantCreatedEvent event) {
        tenantCreatedEventHandler.handle(event);
    }
}
