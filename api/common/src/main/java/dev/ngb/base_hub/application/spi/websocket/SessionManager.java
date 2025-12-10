package dev.ngb.base_hub.application.spi.websocket;

import java.util.Set;

/**
 * Interface for managing WebSocket sessions.
 * Provides methods to register/unregister sessions and query active sessions.
 */
public interface SessionManager {
    /**
     * Registers a WebSocket session for a user.
     *
     * @param userId     the user ID
     * @param sessionId  the session ID
     * @param instanceId the instance ID of the gateway instance managing this session
     */
    void registerSession(String userId, String sessionId, String instanceId);

    /**
     * Unregisters a WebSocket session for a user.
     *
     * @param userId    the user ID
     * @param sessionId the session ID
     */
    void unregisterSession(String userId, String sessionId);

    /**
     * Gets all active session IDs for a user.
     *
     * @param userId the user ID
     * @return set of active session IDs
     */
    Set<String> getUserSessions(String userId);

    /**
     * Gets all user IDs that belong to a group.
     *
     * @param groupId the group ID
     * @return set of user IDs in the group
     */
    Set<String> getGroupUsers(String groupId);

    /**
     * Gets the instance ID that manages a specific session.
     *
     * @param userId    the user ID
     * @param sessionId the session ID
     * @return the instance ID, or null if session not found
     */
    String getSessionInstance(String userId, String sessionId);
}

