package dev.ngb.base_hub.ws_gateway.infrastructure.redis;

import dev.ngb.base_hub.common.annotation.InfraService;
import dev.ngb.base_hub.application.spi.websocket.SessionManager;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMap;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis-based implementation of SessionManager for horizontal scaling.
 * Uses Redis to store session mappings across multiple gateway instances.
 */
@Slf4j
@InfraService
public class RedisSessionManager implements SessionManager {

    private static final String USER_SESSIONS_PREFIX = "ws:user:sessions:";
    private static final String SESSION_INSTANCE_PREFIX = "ws:session:instance:";
    private static final String GROUP_USERS_PREFIX = "ws:group:users:";
    private static final long SESSION_TTL_SECONDS = 3600; // 1 hour

    private final RedissonClient redissonClient;
    private final String instanceId;

    public RedisSessionManager(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
        // Generate a unique instance ID (could be improved with proper instance identification)
        this.instanceId = java.util.UUID.randomUUID().toString();
        log.info("Initialized RedisSessionManager with instanceId: {}", instanceId);
    }

    @Override
    public void registerSession(String userId, String sessionId, String instanceId) {
        String userSessionsKey = USER_SESSIONS_PREFIX + userId;
        String sessionInstanceKey = SESSION_INSTANCE_PREFIX + userId + ":" + sessionId;

        RSet<String> userSessions = redissonClient.getSet(userSessionsKey);
        userSessions.add(sessionId);
        userSessions.expire(SESSION_TTL_SECONDS, TimeUnit.SECONDS);

        RMap<String, String> sessionInstance = redissonClient.getMap(sessionInstanceKey);
        sessionInstance.put(sessionId, instanceId);
        sessionInstance.expire(SESSION_TTL_SECONDS, TimeUnit.SECONDS);

        log.debug("Registered session: userId={}, sessionId={}, instanceId={}", userId, sessionId, instanceId);
    }

    @Override
    public void unregisterSession(String userId, String sessionId) {
        String userSessionsKey = USER_SESSIONS_PREFIX + userId;
        String sessionInstanceKey = SESSION_INSTANCE_PREFIX + userId + ":" + sessionId;

        RSet<String> userSessions = redissonClient.getSet(userSessionsKey);
        userSessions.remove(sessionId);

        RMap<String, String> sessionInstance = redissonClient.getMap(sessionInstanceKey);
        sessionInstance.remove(sessionId);

        log.debug("Unregistered session: userId={}, sessionId={}", userId, sessionId);
    }

    @Override
    public Set<String> getUserSessions(String userId) {
        String userSessionsKey = USER_SESSIONS_PREFIX + userId;
        RSet<String> userSessions = redissonClient.getSet(userSessionsKey);
        return userSessions.readAll();
    }

    @Override
    public Set<String> getGroupUsers(String groupId) {
        String groupUsersKey = GROUP_USERS_PREFIX + groupId;
        RSet<String> groupUsers = redissonClient.getSet(groupUsersKey);
        return groupUsers.readAll();
    }

    @Override
    public String getSessionInstance(String userId, String sessionId) {
        String sessionInstanceKey = SESSION_INSTANCE_PREFIX + userId + ":" + sessionId;
        RMap<String, String> sessionInstance = redissonClient.getMap(sessionInstanceKey);
        return sessionInstance.get(sessionId);
    }

    /**
     * Gets the current instance ID.
     *
     * @return the instance ID
     */
    public String getInstanceId() {
        return instanceId;
    }

    /**
     * Registers a user as a member of a group.
     * This should be called when a user joins a group.
     *
     * @param groupId the group ID
     * @param userId  the user ID
     */
    public void addUserToGroup(String groupId, String userId) {
        String groupUsersKey = GROUP_USERS_PREFIX + groupId;
        RSet<String> groupUsers = redissonClient.getSet(groupUsersKey);
        groupUsers.add(userId);
        log.debug("Added user {} to group {}", userId, groupId);
    }

    /**
     * Removes a user from a group.
     * This should be called when a user leaves a group.
     *
     * @param groupId the group ID
     * @param userId  the user ID
     */
    public void removeUserFromGroup(String groupId, String userId) {
        String groupUsersKey = GROUP_USERS_PREFIX + groupId;
        RSet<String> groupUsers = redissonClient.getSet(groupUsersKey);
        groupUsers.remove(userId);
        log.debug("Removed user {} from group {}", userId, groupId);
    }
}

