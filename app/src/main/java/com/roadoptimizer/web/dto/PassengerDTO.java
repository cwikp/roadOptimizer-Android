package com.roadoptimizer.web.dto;

import lombok.Data;
import lombok.experimental.Builder;

@Data
@Builder
public class PassengerDTO {
    private String firstName;
    private String lastName;
    private LocationDTO location;
    private String rideTime;
}
