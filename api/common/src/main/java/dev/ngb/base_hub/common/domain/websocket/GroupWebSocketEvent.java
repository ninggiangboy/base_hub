package dev.ngb.base_hub.common.domain.websocket;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * WebSocket event targeting a group of users.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GroupWebSocketEvent extends WebSocketEvent {
    /**
     * The target group ID to deliver the message to.
     */
    private String targetGroupId;

    @Override
    public String getType() {
        return "GROUP";
    }
}

