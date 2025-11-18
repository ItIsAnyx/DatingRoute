package com.morzevichka.backend_api.domain.model;

import com.morzevichka.backend_api.domain.value.InnerContext;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Context {
    private Long id;
    private Chat chat;
    private List<InnerContext> innerContexts = new ArrayList<>();
}
