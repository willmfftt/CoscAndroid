package com.cosc.bandfanapp.network.service;

import com.cosc.bandfanapp.network.model.band.CreateBandRequest;
import com.cosc.bandfanapp.network.model.band.CreateBandResponse;
import com.cosc.bandfanapp.network.model.band.GetBandsResponse;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * @author William Moffitt
 * @version 1.0 11/18/15
 */
public interface BandService {

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/band")
    Call<CreateBandResponse> create(@Body CreateBandRequest request);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/band")
    Call<GetBandsResponse> readAll(@Query("username") String username
            , @Query("password") String password);

}
