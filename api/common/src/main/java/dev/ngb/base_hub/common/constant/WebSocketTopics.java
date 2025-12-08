package dev.ngb.base_hub.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Kafka topic names for WebSocket Gateway events.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WebSocketTopics {
    /**
     * Topic for WebSocket events that need to be delivered to users.
     */
    public static final String WEBSOCKET_EVENTS = "websocket-events";
}

