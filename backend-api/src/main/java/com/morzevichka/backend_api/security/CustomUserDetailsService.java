package com.morzevichka.backend_api.security;

import com.morzevichka.backend_api.dto.cache.CachedUser;
import com.morzevichka.backend_api.entity.User;
import com.morzevichka.backend_api.mapper.CachedUserMapper;
import com.morzevichka.backend_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;
    private final CachedUserMapper cachedUserMapper;
    private final RedisTemplate<String, CachedUser> redisTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CachedUser cachedUser = redisTemplate.opsForValue().get(username);

        if (!Objects.isNull(cachedUser)) {
            return new CustomUserDetails(cachedUserMapper.toUser(cachedUser));
        }

        User user = userService.getUserByEmail(username);

        redisTemplate.opsForValue().set(username, cachedUserMapper.toCache(user));

        return new CustomUserDetails(user);
    }
}
