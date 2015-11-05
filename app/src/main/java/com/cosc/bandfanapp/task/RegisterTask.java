package com.cosc.bandfanapp.task;

import android.os.AsyncTask;
import android.util.Log;

import com.cosc.bandfanapp.model.User;
import com.cosc.bandfanapp.network.model.register.RegisterRequest;
import com.cosc.bandfanapp.network.model.register.RegisterResponse;
import com.cosc.bandfanapp.network.service.RegisterService;
import com.cosc.bandfanapp.network.service.RestServiceProvider;
import com.cosc.bandfanapp.util.ErrorCode;

import java.io.IOException;

import retrofit.Call;
import retrofit.Response;

/**
 * @author William Moffitt
 * @version 1.0 10/28/15
 */
public class RegisterTask extends AsyncTask<Void, Void, Void> {

    public interface RegisterListener {
        void registerSuccessful(User user);
        void registerFailed(ErrorCode code);
        void registerCancelled();
    }

    private static final String TAG = "RegisterTask";

    private String mFirstName;
    private String mLastName;
    private String mUsername;
    private String mPassword;
    private String mEmail;

    private RegisterListener mListener;

    public RegisterTask(String firstName, String lastName, String username
            , String password, String email, RegisterListener listener) {
        mFirstName = firstName;
        mLastName = lastName;
        mUsername = username;
        mPassword = password;
        mEmail = email;
        mListener = listener;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        RegisterService service = RestServiceProvider.getRegisterService();

        RegisterRequest request = new RegisterRequest();
        request.first_name = mFirstName;
        request.last_name = mLastName;
        request.username = mUsername;
        request.password = mPassword;
        request.dob = "0000-00-00";
        request.email = mEmail;

        Call<RegisterResponse> call = service.register(request);
        Response<RegisterResponse> response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        if (response != null) {
            if (response.isSuccess()) {
                RegisterResponse registerResponse = response.body();
                if (registerResponse.error) {
                    ErrorCode.Code code = ErrorCode.getCode(registerResponse.code);
                    ErrorCode errorCode = new ErrorCode(code);

                    mListener.registerFailed(errorCode);
                } else {
                    User user = new User();
                    user.setId((long) registerResponse.id);
                    user.setFirstName(mFirstName);
                    user.setLastName(mLastName);
                    user.setUsername(mUsername);
                    user.setPassword(mPassword);
                    user.setDob("0000-00-00");
                    user.setEmail(mEmail);
                    user.setIsModerator(0);

                    mListener.registerSuccessful(user);
                }
            } else {
                ErrorCode errorCode = new ErrorCode(ErrorCode.Code.GENERIC_ERROR);

                mListener.registerFailed(errorCode);
            }
        }

        return null;
    }

    @Override
    protected void onCancelled() {
        mListener.registerCancelled();
    }

}
