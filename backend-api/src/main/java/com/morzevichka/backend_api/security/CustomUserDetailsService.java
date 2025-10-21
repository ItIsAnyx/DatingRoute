package com.morzevichka.backend_api.security;

import com.morzevichka.backend_api.dto.cache.CachedUser;
import com.morzevichka.backend_api.entity.User;
import com.morzevichka.backend_api.mapper.CachedUserMapper;
import com.morzevichka.backend_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final CachedUserMapper cachedUserMapper;
    private final RedisTemplate<String, CachedUser> redisTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (redisTemplate.hasKey(username)) {
            CachedUser cachedUser = redisTemplate.opsForValue().get(username);
            return new CustomUserDetails(cachedUserMapper.toUser(cachedUser));
        }

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        CachedUser cachedUser = cachedUserMapper.toCache(user);
        redisTemplate.opsForValue().setIfAbsent(username, cachedUser);

        return new CustomUserDetails(user);
    }
}
