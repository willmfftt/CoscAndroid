package com.cosc.bandfanapp.task;

import android.os.AsyncTask;
import android.util.Log;

import com.cosc.bandfanapp.model.BandMember;
import com.cosc.bandfanapp.model.User;
import com.cosc.bandfanapp.network.model.bandmember.ReadBandMembersRequest;
import com.cosc.bandfanapp.network.model.bandmember.ReadBandMembersResponse;
import com.cosc.bandfanapp.network.service.BandMemberService;
import com.cosc.bandfanapp.network.service.RestServiceProvider;
import com.orm.SugarRecord;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import retrofit.Call;
import retrofit.Response;

/**
 * @author William Moffitt
 * @version 1.0 11/19/15
 */
public class ReadBandMembersTask extends AsyncTask<Void, Void, Void> {

    public interface OnReadBandMembersListener {
        void onReadBandMembers(List<BandMember> bandMembers);
        void onReadBandMembersFailure();
    }

    private static final String TAG = "ReadBandMembersTask";

    private OnReadBandMembersListener mListener;

    private int mBandId;

    public ReadBandMembersTask(int bandId, OnReadBandMembersListener listener) {
        mBandId = bandId;
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
            mListener.onReadBandMembersFailure();
            return null;
        }

        ReadBandMembersRequest request = new ReadBandMembersRequest();
        request.username = user.getUsername();
        request.password = user.getPassword();
        request.band_id = mBandId;

        Call<ReadBandMembersResponse> call = service.readBandMembers(request.username
                , request.password, request.band_id);
        Response<ReadBandMembersResponse> response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        if (response != null) {
            if (response.isSuccess()) {
                ReadBandMembersResponse readBandMembersResponse = response.body();
                if (readBandMembersResponse.error) {
                    mListener.onReadBandMembersFailure();
                } else {
                    mListener.onReadBandMembers(readBandMembersResponse.band_members);
                }
            } else {
                mListener.onReadBandMembersFailure();
            }
        } else {
            mListener.onReadBandMembersFailure();
        }

        return null;
    }

}
