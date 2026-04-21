package com.pigeonskyrace.security;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Validates JWT from the STOMP {@code CONNECT} frame ({@code Authorization: Bearer ...}) and sets the
 * principal for the WebSocket session. Unauthenticated CONNECT attempts are rejected.
 */
@Component
@RequiredArgsConstructor
public class StompJwtChannelInterceptor implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null) {
            return message;
        }

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = resolveBearer(accessor);
            if (token == null || jwtTokenProvider.resolveTokenState(token) != JwtTokenProvider.TokenState.VALID) {
                throw new AccessDeniedException("Invalid or missing JWT on STOMP CONNECT");
            }
            String email = jwtTokenProvider.getEmail(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            accessor.setUser(auth);
        }

        return message;
    }

    private String resolveBearer(StompHeaderAccessor accessor) {
        List<String> headers = accessor.getNativeHeader("Authorization");
        if (headers == null || headers.isEmpty()) {
            return null;
        }
        String h = headers.get(0);
        if (h != null && h.regionMatches(true, 0, "Bearer ", 0, 7)) {
            return h.substring(7).trim();
        }
        return null;
    }
}
