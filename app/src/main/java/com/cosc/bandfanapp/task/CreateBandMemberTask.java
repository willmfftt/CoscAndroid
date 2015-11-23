package com.cosc.bandfanapp.task;

import android.os.AsyncTask;
import android.util.Log;

import com.cosc.bandfanapp.model.User;
import com.cosc.bandfanapp.network.model.bandmember.CreateBandMemberRequest;
import com.cosc.bandfanapp.network.model.bandmember.CreateBandMemberResponse;
import com.cosc.bandfanapp.network.service.BandMemberService;
import com.cosc.bandfanapp.network.service.RestServiceProvider;
import com.orm.SugarRecord;

import java.io.IOException;
import java.util.Iterator;

import retrofit.Call;
import retrofit.Response;

/**
 * @author William Moffitt
 * @version 1.0 11/19/15
 */
public class CreateBandMemberTask extends AsyncTask<Void, Void, Void> {

    public interface OnCreateBandMemberListener {
        void onCreateBandMember(int id);
        void onCreateBandMemberFailure();
    }

    private static final String TAG = "CreateBandMemberTask";

    private OnCreateBandMemberListener mListener;

    private String mFirstName;
    private String mLastName;
    private int mBandId;
    private String mDateStart;

    public CreateBandMemberTask(String firstName, String lastName, int bandId, String dateStart
            , OnCreateBandMemberListener listener) {
        mFirstName = firstName;
        mLastName = lastName;
        mBandId = bandId;
        mDateStart = dateStart;
        mListener = listener;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        BandMemberService service = RestServiceProvider.getBandMemberService();

        Iterator<User> users = SugarRecord.findAll(User.class);
        User user = null;
        while (users.hasNext()) {
            user = users.next();
        }

        if (user == null) {
            mListener.onCreateBandMemberFailure();
            return null;
        }

        CreateBandMemberRequest request = new CreateBandMemberRequest();
        request.username = user.getUsername();
        request.password = user.getPassword();
        request.first_name = mFirstName;
        request.last_name = mLastName;
        request.band_id = mBandId;
        request.date_start = mDateStart;

        Call<CreateBandMemberResponse> call = service.create(request);
        Response<CreateBandMemberResponse> response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        if (response != null) {
            if (response.isSuccess()) {
                CreateBandMemberResponse createBandMemberResponse = response.body();
                if (createBandMemberResponse.error) {
                    mListener.onCreateBandMemberFailure();
                } else {
                    mListener.onCreateBandMember(createBandMemberResponse.id);
                }
            } else {
                mListener.onCreateBandMemberFailure();
            }
        } else {
            mListener.onCreateBandMemberFailure();
        }

        return null;
    }

}
