package dev.ngb.base_hub.common.config.rabbit_mq;

import com.rabbitmq.client.Channel;
import dev.ngb.base_hub.common.api.tenant.TenantContextHolder;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;

public class TenantAwareMessageListenerAdapter extends MessageListenerAdapter {

    private final TenantContextHolder tenantContextHolder;

    public TenantAwareMessageListenerAdapter(Object delegate, TenantContextHolder tenantContextHolder) {
        super(delegate);
        this.tenantContextHolder = tenantContextHolder;
    }

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        String tenantId = (String) message.getMessageProperties().getHeaders().get("tenantId");
        try {
            tenantContextHolder.setCurrentTenantId(tenantId);
            super.onMessage(message, channel);
        } finally {
            tenantContextHolder.clear();
        }
    }
}
