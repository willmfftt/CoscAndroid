package com.cosc.bandfanapp.network.service;

import com.cosc.bandfanapp.network.model.register.RegisterRequest;
import com.cosc.bandfanapp.network.model.register.RegisterResponse;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.Headers;
import retrofit.http.POST;

/**
 * @author William Moffitt
 * @version 1.0 10/28/15
 */
public interface RegisterService {

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/register")
    Call<RegisterResponse> register(@Body RegisterRequest request);

}
