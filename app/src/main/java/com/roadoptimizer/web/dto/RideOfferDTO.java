package com.roadoptimizer.web.dto;


import lombok.Data;
import lombok.experimental.Builder;

@Data
@Builder
public class RideOfferDTO {
    private String rideDate;
    private int seats;
    private LocationDTO start;
    private LocationDTO end;
}

