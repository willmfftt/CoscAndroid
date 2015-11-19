package com.cosc.bandfanapp.network.service;

import com.cosc.bandfanapp.Constants;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * @author William Moffitt
 * @version 1.0 10/5/15
 */
public class RestServiceProvider {

    private RestServiceProvider() {
    }

    private static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(Constants.REST_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static LoginService getLoginService() {
        Retrofit retrofit = getRetrofit();

        return retrofit.create(LoginService.class);
    }

    public static RegisterService getRegisterService() {
        Retrofit retrofit = getRetrofit();

        return retrofit.create(RegisterService.class);
    }

    public static BandService getBandService() {
        Retrofit retrofit = getRetrofit();

        return retrofit.create(BandService.class);
    }

}
