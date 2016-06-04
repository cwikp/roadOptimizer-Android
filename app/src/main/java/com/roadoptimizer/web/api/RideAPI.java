package com.roadoptimizer.web.api;

import com.roadoptimizer.web.dto.PassengerDTO;
import com.roadoptimizer.web.dto.RideOfferDTO;
import com.roadoptimizer.web.dto.UserDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RideAPI {

    @POST("api/offers")
    Call<Void> createRideOffer(@Body RideOfferDTO rideOfferDTO);

    @POST("api/candidates")
    Call<Void> joinRideOffer(@Body PassengerDTO passengerDTO);
}
