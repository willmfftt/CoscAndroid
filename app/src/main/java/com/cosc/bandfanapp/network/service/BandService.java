package com.cosc.bandfanapp.network.service;

import com.cosc.bandfanapp.network.model.band.CreateBandRequest;
import com.cosc.bandfanapp.network.model.band.CreateBandResponse;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.Headers;
import retrofit.http.POST;

/**
 * @author William Moffitt
 * @version 1.0 11/18/15
 */
public interface BandService {

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/band")
    Call<CreateBandResponse> create(@Body CreateBandRequest request);

}
