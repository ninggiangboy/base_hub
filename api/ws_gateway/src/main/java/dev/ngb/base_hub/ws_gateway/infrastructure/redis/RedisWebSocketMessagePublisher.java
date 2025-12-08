package dev.ngb.base_hub.ws_gateway.infrastructure.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ngb.base_hub.base.annotation.InfraService;
import dev.ngb.base_hub.common.domain.websocket.WebSocketEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;

/**
 * Publishes WebSocket messages to Redis pub/sub channel for cross-instance synchronization.
 * When an event needs to be delivered to a user/group, it's published to Redis,
 * and all instances listen to determine if they should deliver the message.
 */
@Slf4j
@InfraService
@RequiredArgsConstructor
public class RedisWebSocketMessagePublisher {

    private static final String USER_MESSAGE_CHANNEL_PREFIX = "ws:message:user:";
    private static final String GROUP_MESSAGE_CHANNEL_PREFIX = "ws:message:group:";

    private final RedissonClient redissonClient;
    private final ObjectMapper objectMapper;

    /**
     * Publishes a message to Redis for a specific user.
     *
     * @param userId the target user ID
     * @param event  the WebSocket event
     */
    public void publishToUser(String userId, WebSocketEvent event) {
        try {
            String channel = USER_MESSAGE_CHANNEL_PREFIX + userId;
            String message = objectMapper.writeValueAsString(event);
            RTopic topic = redissonClient.getTopic(channel, JsonJacksonCodec.INSTANCE);
            topic.publish(message);
            log.debug("Published message to Redis channel for user: {}", userId);
        } catch (JsonProcessingException e) {
            log.error("Error serializing WebSocket event for Redis", e);
            throw new RuntimeException("Failed to publish message to Redis", e);
        }
    }

    /**
     * Publishes a message to Redis for a specific group.
     *
     * @param groupId the target group ID
     * @param event   the WebSocket event
     */
    public void publishToGroup(String groupId, WebSocketEvent event) {
        try {
            String channel = GROUP_MESSAGE_CHANNEL_PREFIX + groupId;
            String message = objectMapper.writeValueAsString(event);
            RTopic topic = redissonClient.getTopic(channel, JsonJacksonCodec.INSTANCE);
            topic.publish(message);
            log.debug("Published message to Redis channel for group: {}", groupId);
        } catch (JsonProcessingException e) {
            log.error("Error serializing WebSocket event for Redis", e);
            throw new RuntimeException("Failed to publish message to Redis", e);
        }
    }
}

