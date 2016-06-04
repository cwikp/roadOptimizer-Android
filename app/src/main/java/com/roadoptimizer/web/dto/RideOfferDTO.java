package com.roadoptimizer.web.dto;


import lombok.Data;
import lombok.experimental.Builder;

@Data
@Builder
public class RideOfferDTO {
    private Long date;
    private int sits;
    private LocationDTO location;
}

