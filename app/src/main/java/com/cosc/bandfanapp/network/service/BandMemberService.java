package com.cosc.bandfanapp.network.service;

import com.cosc.bandfanapp.network.model.bandmember.CreateBandMemberRequest;
import com.cosc.bandfanapp.network.model.bandmember.CreateBandMemberResponse;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.Headers;
import retrofit.http.POST;

/**
 * @author William Moffitt
 * @version 1.0 11/19/15
 */
public interface BandMemberService {

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/band-member")
    Call<CreateBandMemberResponse> create(@Body CreateBandMemberRequest request);

}
