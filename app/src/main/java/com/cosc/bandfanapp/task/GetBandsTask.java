package com.cosc.bandfanapp.task;

import android.os.AsyncTask;
import android.util.Log;

import com.cosc.bandfanapp.model.User;
import com.cosc.bandfanapp.network.model.band.Band;
import com.cosc.bandfanapp.network.model.band.GetBandsResponse;
import com.cosc.bandfanapp.network.service.BandService;
import com.cosc.bandfanapp.network.service.RestServiceProvider;
import com.orm.SugarRecord;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import retrofit.Call;
import retrofit.Response;

/**
 * @author William Moffitt
 * @version 1.0 11/18/15
 */
public class GetBandsTask extends AsyncTask<Void, Void, Void> {

    public interface OnGetBandsResponseListener {
        void onGetBandsResponse(List<Band> bands);
        void onGetBandsFailure();
    }

    private static final String TAG = "GetBandsTask";

    private OnGetBandsResponseListener mListener;

    public GetBandsTask(OnGetBandsResponseListener listener) {
        mListener = listener;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        BandService service = RestServiceProvider.getBandService();

        Iterator<User> users = SugarRecord.findAll(User.class);
        User user = null;
        while (users.hasNext()) {
            user = users.next();
        }

        if (user == null) {
            mListener.onGetBandsFailure();
            return null;
        }

        Call<GetBandsResponse> call = service.readAll(user.getUsername(), user.getPassword());
        Response<GetBandsResponse> response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        if (response != null) {
            if (response.isSuccess()) {
                GetBandsResponse getBandsResponse = response.body();
                if (getBandsResponse.error) {
                    mListener.onGetBandsFailure();
                } else {
                    mListener.onGetBandsResponse(getBandsResponse.bands);
                }
            }
        } else {
            mListener.onGetBandsFailure();
        }

        return null;
    }

}
