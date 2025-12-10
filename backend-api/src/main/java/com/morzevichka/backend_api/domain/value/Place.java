package com.morzevichka.backend_api.domain.value;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Place implements Serializable {
    private String address;
    private Double lat;
    private Double lon;
}
