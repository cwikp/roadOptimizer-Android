package com.roadoptimizer.web.api;

import com.roadoptimizer.web.dto.RideOfferDTO;
import com.roadoptimizer.web.dto.UserDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RideAPI {

    @POST("api/ride")
    Call<RideOfferDTO> createRide(@Body RideOfferDTO rideOfferDTO);
}
