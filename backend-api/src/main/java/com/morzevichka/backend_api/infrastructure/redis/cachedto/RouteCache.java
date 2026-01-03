package com.morzevichka.backend_api.infrastructure.redis.cachedto;

import com.morzevichka.backend_api.domain.value.Place;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

@RedisHash("places")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RouteCache implements Serializable {
    @Id
    private UUID id;
    private Map<UUID, Place> places;
    @TimeToLive
    private Long ttl = 30 * 24 * 60 * 60L;

    public RouteCache(UUID id, Map<UUID, Place> places) {
        this.id = id;
        this.places = places;
    }
}
