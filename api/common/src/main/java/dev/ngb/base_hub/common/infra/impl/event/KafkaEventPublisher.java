package dev.ngb.base_hub.common.infra.impl.event;

import dev.ngb.base_hub.common.api.event.EventPublisher;
import dev.ngb.base_hub.common.api.tenant.OrganizationContextHolder;
import dev.ngb.base_hub.common.base.annotation.InfraService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;

@InfraService
@RequiredArgsConstructor
public class KafkaEventPublisher implements EventPublisher {

    private final OrganizationContextHolder organizationContextHolder;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC_NAME = "events-topic";

    @Override
    public void publish(Object event) {
        String key = event.getClass().getSimpleName();
        // Build message with key and tenantId
        ProducerRecord<String, Object> record = new ProducerRecord<>(TOPIC_NAME, key, event);
        record.headers().add("tenantId", organizationContextHolder.getCurrentOrgId().getBytes());
        kafkaTemplate.send(record);
    }
}
