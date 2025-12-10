package com.morzevichka.backend_api.infrastructure.redis.cachedto;

import com.morzevichka.backend_api.domain.value.RouteType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RedisHash("routes")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RouteCache implements Serializable {
    @Id
    private Long id;
    @Indexed
    private Long chatId;
    private List<String> places = new ArrayList<>();
    private LocalDateTime updatedAt;
    private RouteType type = RouteType.PEDESTRIAN;
    @TimeToLive
    private Long ttl = 12 * 60 * 60L;
}
