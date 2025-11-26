package dev.ngb.base_hub.common.infra.impl.event;

import dev.ngb.base_hub.common.api.event.EventPublisher;
import dev.ngb.base_hub.common.api.tenant.TenantContextHolder;
import dev.ngb.base_hub.common.base.annotation.InfraService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@InfraService
@RequiredArgsConstructor
public class RabbitMQEventPublisher implements EventPublisher {

    private final TenantContextHolder tenantContextHolder;
    private final RabbitTemplate rabbitTemplate;
    private static final String EXCHANGE_NAME = "events.exchange";

    @Override
    public void publish(Object event) {
        String routingKey = event.getClass().getSimpleName();
        MessagePostProcessor messagePostProcessor = message -> {
            message.getMessageProperties().setHeader("tenantId", tenantContextHolder.getCurrentTenantId());
            return message;
        };
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, routingKey, event, messagePostProcessor);
    }
}
