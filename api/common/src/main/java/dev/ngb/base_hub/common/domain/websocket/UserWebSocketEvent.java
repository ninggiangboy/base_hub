package dev.ngb.base_hub.common.domain.websocket;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * WebSocket event targeting a specific user.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserWebSocketEvent extends WebSocketEvent {
    /**
     * The target user ID to deliver the message to.
     */
    private String targetUserId;

    @Override
    public String getType() {
        return "USER";
    }
}

