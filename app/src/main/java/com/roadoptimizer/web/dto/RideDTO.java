package com.roadoptimizer.web.dto;

import java.util.List;

import lombok.Data;
import lombok.experimental.Builder;

@Data
@Builder
public class RideDTO {
    private List<LocationDTO> checkpoints;
    private String rideTime;
    private List<PassengerDTO> passengers;
    private LocationDTO start;
    private LocationDTO end;
}
