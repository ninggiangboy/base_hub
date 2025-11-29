package dev.ngb.base_hub.common.config.rabbit_mq;

import com.rabbitmq.client.Channel;
import dev.ngb.base_hub.common.api.tenant.OrganizationContextHolder;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;

public class OrganizationAwareMessageListenerAdapter extends MessageListenerAdapter {

    private final OrganizationContextHolder organizationContextHolder;

    public OrganizationAwareMessageListenerAdapter(Object delegate, OrganizationContextHolder organizationContextHolder) {
        super(delegate);
        this.organizationContextHolder = organizationContextHolder;
    }

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        String tenantId = (String) message.getMessageProperties().getHeaders().get("orgId");
        try {
            organizationContextHolder.setCurrentOrgId(tenantId);
            super.onMessage(message, channel);
        } finally {
            organizationContextHolder.clear();
        }
    }
}
