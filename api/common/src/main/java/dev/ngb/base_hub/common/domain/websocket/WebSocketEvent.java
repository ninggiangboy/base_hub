package dev.ngb.base_hub.common.domain.websocket;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.ngb.base_hub.base.event.IntegrationEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Base event model for WebSocket Gateway.
 * Each event contains either targetUserId or targetGroupId to identify the target recipients.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = UserWebSocketEvent.class, name = "USER"),
    @JsonSubTypes.Type(value = GroupWebSocketEvent.class, name = "GROUP")
})
public abstract class WebSocketEvent implements IntegrationEvent {
    /**
     * The message payload to be delivered to WebSocket clients.
     */
    private Object payload;

    /**
     * Optional metadata associated with the event.
     */
    private Object metadata;

    /**
     * Event type identifier.
     */
    public abstract String getType();
}

