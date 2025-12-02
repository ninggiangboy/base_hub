package dev.ngb.base_hub.admin.coordination.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ngb.base_hub.admin.shared.organization.event.OrganizationCreatedEvent;
import dev.ngb.base_hub.common.api.event.EventMediator;
import dev.ngb.base_hub.common.base.annotation.Adapter;
import dev.ngb.base_hub.common.config.kafka.JsonKafkaListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
@RequiredArgsConstructor
public class CoordinatorConsumer {

    private final EventMediator eventMediator;

    @JsonKafkaListener(topics = OrganizationCreatedEvent.TOPIC)
    public void handle(OrganizationCreatedEvent event) {
        eventMediator.handle(event);
    }
}
