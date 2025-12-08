package dev.ngb.base_hub.ws_gateway.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ngb.base_hub.base.annotation.InfraService;
import dev.ngb.base_hub.common.domain.websocket.GroupWebSocketEvent;
import dev.ngb.base_hub.common.domain.websocket.UserWebSocketEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Configuration for WebSocket Gateway infrastructure components.
 */
@Configuration
@InfraService
public class WebSocketGatewayConfig {

    /**
     * Creates an ObjectMapper configured for WebSocket event serialization/deserialization.
     * Includes type information for polymorphic deserialization of WebSocketEvent subclasses.
     */
    @Bean
    @Primary
    public ObjectMapper webSocketObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerSubtypes(UserWebSocketEvent.class, GroupWebSocketEvent.class);
        return mapper;
    }
}

