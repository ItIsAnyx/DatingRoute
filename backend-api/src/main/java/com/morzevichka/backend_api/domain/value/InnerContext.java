package com.morzevichka.backend_api.domain.value;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InnerContext {
    @JsonProperty("role")
    private ContextType type;
    private String content;
}
