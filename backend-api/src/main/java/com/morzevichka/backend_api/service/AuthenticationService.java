package com.morzevichka.backend_api.service;

import com.morzevichka.backend_api.dto.authentication.*;
import com.morzevichka.backend_api.dto.cache.CachedUser;
import com.morzevichka.backend_api.entity.User;
import com.morzevichka.backend_api.mapper.AuthenticationMapper;
import com.morzevichka.backend_api.mapper.CachedUserMapper;
import com.morzevichka.backend_api.security.CustomUserDetails;
import com.morzevichka.backend_api.security.CustomUserDetailsService;
import com.morzevichka.backend_api.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final AuthenticationMapper authenticationMapper;
    private final RedisTemplate<String, CachedUser> redisTemplate;
    private final CachedUserMapper cachedUserMapper;

    public AuthenticationResponse register(RegisterRequest registerBody) {
        User user = userService.createUser(
                registerBody.login(),
                registerBody.email(),
                registerBody.password()
        );

        CachedUser cachedUser = cachedUserMapper.toCache(user);
        redisTemplate.opsForValue().setIfAbsent(user.getEmail(), cachedUser);

        CustomUserDetails userDetails = new CustomUserDetails(user);

        return authenticationMapper.toDto(
                jwtProvider.generateToken(userDetails),
                jwtProvider.generateRefreshToken(userDetails)
        );
    }

    public AuthenticationResponse login(LoginRequest loginBody) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginBody.email(),
                        loginBody.password()
                )
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        return authenticationMapper.toDto(
                jwtProvider.generateToken(userDetails),
                jwtProvider.generateRefreshToken(userDetails)
        );
    }

    public RefreshResponse refresh(RefreshRequest request) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtProvider.extractUsername(request.refreshToken()));
        return authenticationMapper.toRefreshDto(jwtProvider.generateToken(userDetails));
    }
}
