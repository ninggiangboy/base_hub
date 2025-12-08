# WebSocket Gateway Service

## Overview

The WebSocket Gateway Service is a Spring Boot application that follows Clean Architecture principles. It consumes events from Kafka and delivers them to WebSocket clients, with support for horizontal scaling using Redis as a backplane.

## Architecture

### Clean Architecture Layers

#### Domain Layer (Common Module)
- **Event Models**: `WebSocketEvent`, `UserWebSocketEvent`, `GroupWebSocketEvent`
- **Topic Constants**: `WebSocketTopics`
- **Interfaces**: `WebSocketMessageSender`, `SessionManager`

#### Application Layer (ws_gateway Module)
- **Use Cases**: `RouteEventToWebSocketUseCase` - Routes events to appropriate recipients

#### Infrastructure Layer (ws_gateway Module)
- **Kafka**: `WebSocketEventKafkaListener` - Consumes events from Kafka
- **Redis**: 
  - `RedisSessionManager` - Manages WebSocket sessions across instances
  - `RedisWebSocketMessagePublisher` - Publishes messages to Redis pub/sub
  - `RedisMessageSubscriber` - Subscribes to Redis channels for cross-instance message delivery
- **WebSocket**: 
  - `WebSocketSessionRegistry` - In-memory registry of active sessions
  - `WebSocketMessageSenderImpl` - Implements message delivery

#### Delivery Layer (ws_gateway Module)
- **WebSocket API**: 
  - `WebSocketHandler` - Handles WebSocket connections
  - `WebSocketHandshakeInterceptor` - Extracts userId from handshake
  - `WebSocketConfig` - Spring WebSocket configuration

## Features

### 1. Kafka Event Consumption
- Consumes events from `websocket-events` Kafka topic
- Events contain either `targetUserId` or `targetGroupId`
- Uses `@JsonKafkaListener` annotation for type-safe event handling

### 2. WebSocket Message Delivery
- Delivers messages to specific users via `targetUserId`
- Delivers messages to groups via `targetGroupId`
- Supports multiple WebSocket sessions per user

### 3. Horizontal Scaling Support
- Uses Redis to synchronize WebSocket sessions across multiple instances
- Redis pub/sub channels for cross-instance message delivery
- Each instance only delivers messages to its local sessions

### 4. Session Management
- Tracks active WebSocket sessions in Redis
- Maps sessions to instance IDs for proper routing
- Automatic cleanup of expired sessions (1 hour TTL)

## Usage

### WebSocket Connection

Clients connect to the WebSocket endpoint:
```
ws://localhost:8098/api/ws-gateway/ws?userId=<user-id>
```

Or using headers:
```
X-User-Id: <user-id>
```

### Publishing Events

Publish events to Kafka topic `websocket-events`:

**User Event:**
```json
{
  "type": "USER",
  "targetUserId": "user-123",
  "payload": {
    "message": "Hello, User!",
    "timestamp": "2024-01-01T00:00:00Z"
  },
  "metadata": {}
}
```

**Group Event:**
```json
{
  "type": "GROUP",
  "targetGroupId": "group-456",
  "payload": {
    "message": "Hello, Group!",
    "timestamp": "2024-01-01T00:00:00Z"
  },
  "metadata": {}
}
```

### Group Management

To add users to a group (for group message delivery):
```java
redisSessionManager.addUserToGroup(groupId, userId);
```

## Configuration

### Kafka
Configured in `application-common.properties`:
```properties
spring.kafka.bootstrap-servers=localhost:9095
spring.kafka.consumer.group-id=my-group
```

### Redis
Configured in `application-common.properties`:
```properties
spring.data.redis.host=localhost
spring.data.redis.port=6379
```

### WebSocket Endpoint
- Path: `/ws`
- Full URL: `ws://localhost:8098/api/ws-gateway/ws`

## Implementation Details

### Message Flow

1. **Event arrives from Kafka**
   - `WebSocketEventKafkaListener` receives the event
   - Calls `RouteEventToWebSocketUseCase.execute()`

2. **Event Routing**
   - Use case determines if event is for a user or group
   - Calls `WebSocketMessageSender.sendToUser()` or `sendToGroup()`

3. **Redis Pub/Sub**
   - Message is published to Redis channel (`ws:message:user:<userId>` or `ws:message:group:<groupId>`)
   - All instances receive the message via `RedisMessageSubscriber`

4. **Local Delivery**
   - Each instance checks if it has local sessions for the target user(s)
   - Only delivers to sessions managed by that instance
   - Uses `WebSocketSessionRegistry` to find active sessions

### Session Registration

When a WebSocket connection is established:
1. `WebSocketHandshakeInterceptor` extracts `userId` from query params or headers
2. `WebSocketHandler.afterConnectionEstablished()` is called
3. Session is registered in `WebSocketSessionRegistry` (local)
4. Session is registered in Redis via `RedisSessionManager`
5. Instance subscribes to Redis channel for that user

## Dependencies

- Spring Boot WebSocket (`spring-boot-starter-websocket`)
- Spring Kafka (`spring-boot-kafka`)
- Redisson (`redisson:3.52.0`)
- Jackson for JSON serialization

## Notes

- Group membership must be managed separately (users added to groups via `RedisSessionManager.addUserToGroup()`)
- Session TTL is set to 1 hour - sessions expire automatically
- CORS is currently set to allow all origins (`*`) - configure appropriately for production
- Instance IDs are generated using UUIDs - consider using hostname or container ID in production

