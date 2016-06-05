package com.roadoptimizer.web.services;


import android.util.Log;

import com.roadoptimizer.activities.YourRides;
import com.roadoptimizer.utils.Constants;
import com.roadoptimizer.web.api.LoginAPI;
import com.roadoptimizer.web.api.RideAPI;
import com.roadoptimizer.web.dto.RideDTO;
import com.roadoptimizer.web.dto.UserDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RideService {
    YourRides yourRidesActivity;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Constants.SERVER_ADDRESS)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    LoginAPI userService = retrofit.create(LoginAPI.class);
    RideAPI rideService = retrofit.create(RideAPI.class);

    public RideService(YourRides yourRidesActivity){
        this.yourRidesActivity = yourRidesActivity;
    }

//    public void fillCurrentUserId(Call call) {
//        call.enqueue(new Callback<List<UserDTO>>() {
//
//            @Override
//            public void onResponse(Call<List<UserDTO>> call, Response<List<UserDTO>> response) {
//                yourRidesActivity.setEventsData(response.body());
//                yourRidesActivity.fillEventsListView();
//            }
//
//            @Override
//            public void onFailure(Call<List<UserDTO>> call, Throwable t) {
//                Log.e("MainActivity", t.getMessage(), t);
//
//            }
//
//        });
//    }

    public void fillEventsData(Call call) {
        call.enqueue(new Callback<List<RideDTO>>() {

            @Override
            public void onResponse(Call<List<RideDTO>> call, Response<List<RideDTO>> response) {
                if (response.body() != null) {
                    yourRidesActivity.setEventsData(response.body());
                    yourRidesActivity.fillEventsListView();
                }
            }

            @Override
            public void onFailure(Call<List<RideDTO>> call, Throwable t) {
                Log.e("MainActivity", t.getMessage(), t);

            }

        });
    }
}
