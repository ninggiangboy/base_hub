package dev.ngb.base_hub.ws_gateway.infrastructure.kafka;

import dev.ngb.base_hub.base.annotation.InfraService;
import dev.ngb.base_hub.common.config.kafka.JsonKafkaListener;
import dev.ngb.base_hub.common.constant.WebSocketTopics;
import dev.ngb.base_hub.common.domain.websocket.WebSocketEvent;
import dev.ngb.base_hub.ws_gateway.application.usecase.RouteEventToWebSocketUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;

/**
 * Kafka listener for consuming WebSocket events from Kafka topics.
 */
@Slf4j
@InfraService
@RequiredArgsConstructor
public class WebSocketEventKafkaListener {

    private final RouteEventToWebSocketUseCase routeEventUseCase;

    @JsonKafkaListener(topics = WebSocketTopics.WEBSOCKET_EVENTS, groupId = "ws-gateway-group")
    @KafkaHandler
    public void handleWebSocketEvent(WebSocketEvent event) {
        log.info("Received WebSocket event from Kafka: type={}", event.getType());
        try {
            routeEventUseCase.execute(event);
        } catch (Exception e) {
            log.error("Error processing WebSocket event", e);
            throw e;
        }
    }
}

