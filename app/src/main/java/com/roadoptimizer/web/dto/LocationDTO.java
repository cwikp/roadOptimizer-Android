package com.roadoptimizer.web.dto;

import lombok.Data;
import lombok.experimental.Builder;

@Builder
@Data
public class LocationDTO {
    private Double longitude;
    private Double latitude;

    public static LocationDTO valueOf(double[] location) {
        return new LocationDTO(location[0], location[1]);
    }

    public double[] asArray() {
        return new double[]{longitude, latitude};
    }
}
