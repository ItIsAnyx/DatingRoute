package com.morzevichka.backend_api.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthChannelInterceptorAdapter implements ChannelInterceptor {

    private final AuthenticatorService authenticatorService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel messageChannel) {
        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                final String jwt = authHeader.substring(7);
                try {
                    final UsernamePasswordAuthenticationToken authToken = authenticatorService.getAuthenticatedOfFail(jwt, null);
                    if (authToken != null) {
                        accessor.setUser(authToken);
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                } catch (ExpiredJwtException ex) {
                    throw new MessagingException("JWT Expired", ex);
                } catch (JwtException ex) {
                    throw new MessagingException("Invalid JWT", ex);
                }
            }

        }

        return message;
    }
}
