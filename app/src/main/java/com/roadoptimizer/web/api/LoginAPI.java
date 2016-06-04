package com.roadoptimizer.web.api;

import com.roadoptimizer.web.dto.LoginDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginAPI {

    @POST("api/login")
    Call<LoginDTO> login(@Body LoginDTO user);

}
