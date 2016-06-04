package com.roadoptimizer.web.api;

import com.roadoptimizer.web.dto.UserDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface UserAPI {

    @POST("api/users")
    Call<UserDTO> createUser(@Body UserDTO user);

}
