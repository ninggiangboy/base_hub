package dev.ngb.base_hub.ws_gateway.delivery.websocket;

import dev.ngb.base_hub.base.annotation.Adapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.net.URI;
import java.util.Map;

/**
 * Interceptor for WebSocket handshake requests.
 * Extracts userId from query parameters or headers and stores it in session attributes.
 */
@Slf4j
@Adapter
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

    private static final String USER_ID_PARAM = "userId";
    private static final String USER_ID_HEADER = "X-User-Id";

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes
    ) throws Exception {
        String userId = extractUserId(request);
        if (userId == null || userId.isBlank()) {
            log.warn("WebSocket handshake rejected: userId not provided. URI: {}", request.getURI());
            return false;
        }

        attributes.put("userId", userId);
        log.debug("WebSocket handshake accepted: userId={}", userId);
        return true;
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception
    ) {
        if (exception != null) {
            log.error("WebSocket handshake error", exception);
        }
    }

    /**
     * Extracts userId from the request.
     * Checks query parameters first, then headers.
     *
     * @param request the HTTP request
     * @return the userId, or null if not found
     */
    private String extractUserId(ServerHttpRequest request) {
        // Try query parameter first
        URI uri = request.getURI();
        if (uri != null && uri.getQuery() != null) {
            String[] params = uri.getQuery().split("&");
            for (String param : params) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2 && USER_ID_PARAM.equals(keyValue[0])) {
                    return keyValue[1];
                }
            }
        }

        // Try header
        String userIdHeader = request.getHeaders().getFirst(USER_ID_HEADER);
        if (userIdHeader != null && !userIdHeader.isBlank()) {
            return userIdHeader;
        }

        return null;
    }
}

