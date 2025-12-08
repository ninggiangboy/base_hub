package dev.ngb.base_hub.ws_gateway.infrastructure.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry for managing active WebSocket sessions in memory.
 * Maps session IDs to WebSocketSession objects.
 */
@Slf4j
@Component
public class WebSocketSessionRegistry {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    /**
     * Registers a WebSocket session.
     *
     * @param session the WebSocket session
     */
    public void registerSession(WebSocketSession session) {
        sessions.put(session.getId(), session);
        log.debug("Registered WebSocket session: {}", session.getId());
    }

    /**
     * Unregisters a WebSocket session.
     *
     * @param sessionId the session ID
     */
    public void unregisterSession(String sessionId) {
        sessions.remove(sessionId);
        log.debug("Unregistered WebSocket session: {}", sessionId);
    }

    /**
     * Gets a WebSocket session by ID.
     *
     * @param sessionId the session ID
     * @return the WebSocket session, or null if not found
     */
    public WebSocketSession getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    /**
     * Gets all registered sessions.
     *
     * @return map of session IDs to sessions
     */
    public Map<String, WebSocketSession> getAllSessions() {
        return Map.copyOf(sessions);
    }

    /**
     * Gets the count of active sessions.
     *
     * @return the number of active sessions
     */
    public int getSessionCount() {
        return sessions.size();
    }
}

