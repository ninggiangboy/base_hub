package dev.ngb.base_hub.ws_gateway.infrastructure.config;

import dev.ngb.base_hub.common.annotation.InfraService;
import dev.ngb.base_hub.ws_gateway.delivery.websocket.WebSocketHandler;
import dev.ngb.base_hub.ws_gateway.delivery.websocket.WebSocketHandshakeInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * WebSocket configuration for enabling WebSocket endpoints.
 */
@Configuration
@EnableWebSocket
@InfraService
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketHandler webSocketHandler;
    private final WebSocketHandshakeInterceptor handshakeInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/ws")
                .addInterceptors(handshakeInterceptor)
                .setAllowedOrigins("*"); // Configure allowed origins based on your security requirements
    }
}

