package com.morzevichka.backend_api.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthChannelInterceptorAdapter implements ChannelInterceptor {

    private final AuthenticatorService authenticatorService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel messageChannel) {
        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (Objects.nonNull(accessor) && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (Objects.nonNull(authHeader) && authHeader.startsWith("Bearer ")) {
                final String jwt = authHeader.substring(7);
                try {
                    final UsernamePasswordAuthenticationToken authToken = authenticatorService.getAuthenticatedOfFail(jwt, null);
                    if (Objects.nonNull(authToken)) {
                        accessor.setUser(authToken);
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                } catch (ExpiredJwtException ex) {
                    log.warn("JWT expired: {}", jwt);
                    throw new MessagingException("JWT expired");
                } catch (JwtException ex) {
                    log.warn("Invalid JWT: {}", jwt);
                    throw new MessagingException("Invalid JWT");
                } catch (Exception ex) {
                    log.error("{}", ex.getMessage());
                }
            }

        }

        return message;
    }
}
