package dev.ngb.base_hub.ws_gateway.delivery.websocket;

import dev.ngb.base_hub.base.annotation.Adapter;
import dev.ngb.base_hub.common.api.websocket.SessionManager;
import dev.ngb.base_hub.ws_gateway.infrastructure.redis.RedisMessageSubscriber;
import dev.ngb.base_hub.ws_gateway.infrastructure.redis.RedisSessionManager;
import dev.ngb.base_hub.ws_gateway.infrastructure.websocket.WebSocketSessionRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;

/**
 * WebSocket handler for managing client connections.
 * Handles connection lifecycle and extracts user information from session attributes.
 */
@Slf4j
@Adapter
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private final SessionManager sessionManager;
    private final WebSocketSessionRegistry sessionRegistry;
    private final RedisSessionManager redisSessionManager;
    private final RedisMessageSubscriber redisMessageSubscriber;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String sessionId = session.getId();
        String userId = extractUserId(session);

        if (userId == null || userId.isBlank()) {
            log.warn("WebSocket connection established without userId, closing session: {}", sessionId);
            session.close(CloseStatus.POLICY_VIOLATION.withReason("User ID is required"));
            return;
        }

        sessionRegistry.registerSession(session);
        sessionManager.registerSession(userId, sessionId, redisSessionManager.getInstanceId());
        
        // Subscribe to Redis channel for this user to receive messages from other instances
        redisMessageSubscriber.subscribeToUser(userId);
        
        log.info("WebSocket connection established: userId={}, sessionId={}", userId, sessionId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String sessionId = session.getId();
        String userId = extractUserId(session);

        if (userId != null && !userId.isBlank()) {
            sessionManager.unregisterSession(userId, sessionId);
        }
        sessionRegistry.unregisterSession(sessionId);
        log.info("WebSocket connection closed: userId={}, sessionId={}, status={}", userId, sessionId, status);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Handle incoming messages from clients if needed
        // For now, this is primarily a one-way communication (server to client)
        log.debug("Received message from WebSocket client: sessionId={}, message={}", session.getId(), message.getPayload());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("WebSocket transport error: sessionId={}", session.getId(), exception);
        String userId = extractUserId(session);
        String sessionId = session.getId();
        if (userId != null && !userId.isBlank()) {
            sessionManager.unregisterSession(userId, sessionId);
        }
        sessionRegistry.unregisterSession(sessionId);
    }

    /**
     * Extracts the user ID from WebSocket session attributes.
     * The user ID is set during the WebSocket handshake by WebSocketHandshakeInterceptor.
     *
     * @param session the WebSocket session
     * @return the user ID, or null if not found
     */
    private String extractUserId(WebSocketSession session) {
        Map<String, Object> attributes = session.getAttributes();
        Object userIdObj = attributes.get("userId");
        if (userIdObj != null) {
            return userIdObj.toString();
        }
        return null;
    }
}

