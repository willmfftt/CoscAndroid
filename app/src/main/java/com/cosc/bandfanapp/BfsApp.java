package com.cosc.bandfanapp;

import android.app.Application;

import com.orm.SugarContext;

/**
 * @author William Moffitt
 * @version 1.0 10/29/15
 */
public class BfsApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SugarContext.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        SugarContext.terminate();
    }

}
