package dev.ngb.base_hub.ws_gateway.infrastructure.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ngb.base_hub.common.annotation.InfraService;
import dev.ngb.base_hub.application.spi.websocket.SessionManager;
import dev.ngb.base_hub.common.domain.websocket.WebSocketEvent;
import dev.ngb.base_hub.ws_gateway.infrastructure.websocket.WebSocketSessionRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.redisson.codec.JsonJacksonCodec;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Subscribes to Redis pub/sub channels to receive WebSocket messages from other instances.
 * This enables horizontal scaling by synchronizing message delivery across instances.
 */
@Slf4j
@InfraService
@RequiredArgsConstructor
public class RedisMessageSubscriber {

    private static final String USER_MESSAGE_CHANNEL_PREFIX = "ws:message:user:";
    private static final String GROUP_MESSAGE_CHANNEL_PREFIX = "ws:message:group:";

    private final RedissonClient redissonClient;
    private final SessionManager sessionManager;
    private final WebSocketSessionRegistry sessionRegistry;
    private final RedisSessionManager redisSessionManager;
    private final ObjectMapper objectMapper;

    // Track subscribed channels to avoid duplicate subscriptions
    private final Set<String> subscribedUserChannels = ConcurrentHashMap.newKeySet();
    private final Set<String> subscribedGroupChannels = ConcurrentHashMap.newKeySet();

    /**
     * Subscribes to messages for a specific user.
     * This method is idempotent - multiple calls for the same user will only create one subscription.
     *
     * @param userId the user ID to subscribe for
     */
    public void subscribeToUser(String userId) {
        String channel = USER_MESSAGE_CHANNEL_PREFIX + userId;
        if (subscribedUserChannels.add(channel)) {
            RTopic topic = redissonClient.getTopic(channel, JsonJacksonCodec.INSTANCE);
            topic.addListener(String.class, (MessageListener<String>) (channelName, message) -> {
                try {
                    WebSocketEvent event = objectMapper.readValue(message, WebSocketEvent.class);
                    deliverToLocalSessions(userId, event);
                } catch (Exception e) {
                    log.error("Error processing Redis message for user: {}", userId, e);
                }
            });
            log.debug("Subscribed to Redis channel for user: {}", userId);
        }
    }

    /**
     * Subscribes to messages for a specific group.
     * This method is idempotent - multiple calls for the same group will only create one subscription.
     *
     * @param groupId the group ID to subscribe for
     */
    public void subscribeToGroup(String groupId) {
        String channel = GROUP_MESSAGE_CHANNEL_PREFIX + groupId;
        if (subscribedGroupChannels.add(channel)) {
            RTopic topic = redissonClient.getTopic(channel, JsonJacksonCodec.INSTANCE);
            topic.addListener(String.class, (MessageListener<String>) (channelName, message) -> {
                try {
                    WebSocketEvent event = objectMapper.readValue(message, WebSocketEvent.class);
                    Set<String> userIds = sessionManager.getGroupUsers(groupId);
                    for (String userId : userIds) {
                        deliverToLocalSessions(userId, event);
                    }
                } catch (Exception e) {
                    log.error("Error processing Redis message for group: {}", groupId, e);
                }
            });
            log.debug("Subscribed to Redis channel for group: {}", groupId);
        }
    }

    private void deliverToLocalSessions(String userId, WebSocketEvent event) {
        Set<String> sessionIds = sessionManager.getUserSessions(userId);
        String currentInstanceId = redisSessionManager.getInstanceId();
        int deliveredCount = 0;

        for (String sessionId : sessionIds) {
            String instanceId = sessionManager.getSessionInstance(userId, sessionId);
            if (currentInstanceId.equals(instanceId)) {
                WebSocketSession session = sessionRegistry.getSession(sessionId);
                if (session != null && session.isOpen()) {
                    try {
                        String message = objectMapper.writeValueAsString(event);
                        session.sendMessage(new TextMessage(message));
                        deliveredCount++;
                    } catch (IOException e) {
                        log.error("Error sending message to WebSocket session: {}", sessionId, e);
                    }
                }
            }
        }

        log.debug("Delivered Redis message to {} local sessions for user: {}", deliveredCount, userId);
    }
}

