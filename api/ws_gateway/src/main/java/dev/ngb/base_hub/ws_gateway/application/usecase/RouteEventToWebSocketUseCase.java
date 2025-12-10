package dev.ngb.base_hub.ws_gateway.application.usecase;

import dev.ngb.base_hub.common.annotation.UseCaseService;
import dev.ngb.base_hub.application.spi.websocket.WebSocketMessageSender;
import dev.ngb.base_hub.common.domain.websocket.GroupWebSocketEvent;
import dev.ngb.base_hub.common.domain.websocket.UserWebSocketEvent;
import dev.ngb.base_hub.common.domain.websocket.WebSocketEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Use case for routing WebSocket events to the correct recipients.
 * Handles both user-specific and group-based event routing.
 */
@Slf4j
@UseCaseService
@RequiredArgsConstructor
public class RouteEventToWebSocketUseCase {
    private final WebSocketMessageSender messageSender;

    /**
     * Routes a WebSocket event to the appropriate recipients.
     *
     * @param event the WebSocket event to route
     */
    public void execute(WebSocketEvent event) {
        if (event instanceof UserWebSocketEvent userEvent) {
            String userId = userEvent.getTargetUserId();
            if (userId == null || userId.isBlank()) {
                log.warn("Received UserWebSocketEvent with null or blank targetUserId");
                return;
            }
            log.debug("Routing event to user: {}", userId);
            messageSender.sendToUser(userId, event);
        } else if (event instanceof GroupWebSocketEvent groupEvent) {
            String groupId = groupEvent.getTargetGroupId();
            if (groupId == null || groupId.isBlank()) {
                log.warn("Received GroupWebSocketEvent with null or blank targetGroupId");
                return;
            }
            log.debug("Routing event to group: {}", groupId);
            messageSender.sendToGroup(groupId, event);
        } else {
            log.warn("Received unknown WebSocket event type: {}", event.getClass().getName());
        }
    }
}

