package com.cosc.bandfanapp.task;

import android.os.AsyncTask;
import android.util.Log;

import com.cosc.bandfanapp.model.Band;
import com.cosc.bandfanapp.model.User;
import com.cosc.bandfanapp.network.model.band.CreateBandRequest;
import com.cosc.bandfanapp.network.model.band.CreateBandResponse;
import com.cosc.bandfanapp.network.service.BandService;
import com.cosc.bandfanapp.network.service.RestServiceProvider;
import com.orm.SugarRecord;

import java.io.IOException;
import java.util.Iterator;

import retrofit.Call;
import retrofit.Response;

/**
 * @author William Moffitt
 * @version 1.0 11/18/15
 */
public class CreateBandTask extends AsyncTask<Void, Void, Void> {

    public interface CreateBandListener {
        void onBandCreated(Band band);
        void onBandCreateFailed();
    }

    private static final String TAG = "CreateBandTask";

    private String mName;
    private String mDateStart;
    private CreateBandListener mListener;

    public CreateBandTask(String name, String dateStart, CreateBandListener listener) {
        mName = name;
        mDateStart = dateStart;
        mListener = listener;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        BandService service = RestServiceProvider.getBandService();

        User user = null;
        Iterator<User> users = SugarRecord.findAll(User.class);
        while (users.hasNext()) {
            user = users.next();
        }

        if (user == null) {
            mListener.onBandCreateFailed();
            return null;
        }

        CreateBandRequest request = new CreateBandRequest();
        request.username = user.getUsername();
        request.password = user.getPassword();
        request.name = mName;
        request.date_start = mDateStart;

        Call<CreateBandResponse> call = service.create(request);
        Response<CreateBandResponse> response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        if (response != null) {
            if (response.isSuccess()) {
                CreateBandResponse createBandResponse = response.body();
                if (createBandResponse.error) {
                    mListener.onBandCreateFailed();
                } else {
                    Band band = new Band();
                    band.id = createBandResponse.id;

                    mListener.onBandCreated(band);
                }
            }
        } else {
            mListener.onBandCreateFailed();
        }

        return null;
    }

}
