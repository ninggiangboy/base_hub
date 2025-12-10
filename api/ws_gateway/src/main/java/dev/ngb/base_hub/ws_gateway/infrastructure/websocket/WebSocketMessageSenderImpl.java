package dev.ngb.base_hub.ws_gateway.infrastructure.websocket;

import dev.ngb.base_hub.application.spi.websocket.WebSocketMessageSender;
import dev.ngb.base_hub.common.annotation.InfraService;
import dev.ngb.base_hub.application.spi.websocket.SessionManager;
import dev.ngb.base_hub.common.domain.websocket.WebSocketEvent;
import dev.ngb.base_hub.ws_gateway.infrastructure.redis.RedisMessageSubscriber;
import dev.ngb.base_hub.ws_gateway.infrastructure.redis.RedisWebSocketMessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * Implementation of WebSocketMessageSender that delivers messages to WebSocket clients.
 * Uses Redis pub/sub for cross-instance synchronization.
 */
@Slf4j
@InfraService
@RequiredArgsConstructor
public class WebSocketMessageSenderImpl implements WebSocketMessageSender {

    private final SessionManager sessionManager;
    private final RedisWebSocketMessagePublisher redisPublisher;
    private final RedisMessageSubscriber redisMessageSubscriber;

    @Override
    public void sendToUser(String userId, WebSocketEvent event) {
        Set<String> sessionIds = sessionManager.getUserSessions(userId);
        if (sessionIds.isEmpty()) {
            log.debug("No active sessions found for user: {}", userId);
            return;
        }

        // Publish to Redis pub/sub channel for this user
        // All instances (including this one) will receive the message via RedisMessageSubscriber
        // and deliver to their local sessions
        redisPublisher.publishToUser(userId, event);
    }

    @Override
    public void sendToGroup(String groupId, WebSocketEvent event) {
        Set<String> userIds = sessionManager.getGroupUsers(groupId);
        if (userIds.isEmpty()) {
            log.debug("No users found in group: {}", groupId);
            return;
        }

        // Ensure we're subscribed to this group's channel
        redisMessageSubscriber.subscribeToGroup(groupId);

        // Publish to Redis pub/sub channel for this group
        // All instances (including this one) will receive the message via RedisMessageSubscriber
        // and deliver to their local sessions
        redisPublisher.publishToGroup(groupId, event);
    }

}

