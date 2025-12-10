package dev.ngb.base_hub.application.spi.websocket;

import dev.ngb.base_hub.common.domain.websocket.WebSocketEvent;

/**
 * Interface for sending WebSocket messages to clients.
 * Implementations should execute message delivery to specific users or groups.
 */
public interface WebSocketMessageSender {
    /**
     * Sends a WebSocket event to a specific user.
     *
     * @param userId the target user ID
     * @param event  the WebSocket event to send
     */
    void sendToUser(String userId, WebSocketEvent event);

    /**
     * Sends a WebSocket event to all users in a group.
     *
     * @param groupId the target group ID
     * @param event   the WebSocket event to send
     */
    void sendToGroup(String groupId, WebSocketEvent event);
}

