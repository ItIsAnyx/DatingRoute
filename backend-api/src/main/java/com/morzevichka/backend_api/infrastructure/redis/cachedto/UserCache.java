package com.morzevichka.backend_api.infrastructure.redis.cachedto;

import com.morzevichka.backend_api.domain.value.Role;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@RedisHash("users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCache implements Serializable {
    @Id
    private Long id;
    private String login;
    @Indexed
    private String email;
    private String passwordHash;
    private Role role = Role.USER;
    @TimeToLive
    private Long ttl = 24 * 60 * 60L;
}
