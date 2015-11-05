package com.cosc.bandfanapp.network.service;

import com.cosc.bandfanapp.network.model.login.LoginRequest;
import com.cosc.bandfanapp.network.model.login.LoginResponse;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.Headers;
import retrofit.http.POST;

/**
 * @author William Moffitt
 * @version 1.0 10/28/15
 */
public interface LoginService {

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/login")
    Call<LoginResponse> login(@Body LoginRequest request);

}
