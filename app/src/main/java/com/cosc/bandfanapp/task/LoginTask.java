package com.cosc.bandfanapp.task;

import android.os.AsyncTask;
import android.util.Log;

import com.cosc.bandfanapp.model.User;
import com.cosc.bandfanapp.network.model.login.LoginRequest;
import com.cosc.bandfanapp.network.model.login.LoginResponse;
import com.cosc.bandfanapp.network.service.LoginService;
import com.cosc.bandfanapp.network.service.RestServiceProvider;
import com.cosc.bandfanapp.util.ErrorCode;

import java.io.IOException;

import retrofit.Call;
import retrofit.Response;

/**
 * @author William Moffitt
 * @version 1.0 10/28/15
 */
public class LoginTask extends AsyncTask<Void, Void, Void> {

    public interface LoginListener {
        void loginSuccessful(User user);
        void loginFailed(ErrorCode code);
        void loginCanceled();
    }

    private static final String TAG = "LoginTask";

    private final String mUsername;
    private final String mPassword;
    private final LoginListener mListener;

    public LoginTask(String username, String password, LoginListener listener) {
        mUsername = username;
        mPassword = password;
        mListener = listener;
    }

    @Override
    protected Void doInBackground(Void... params) {
        LoginService service = RestServiceProvider.getLoginService();

        LoginRequest request = new LoginRequest();
        request.username = mUsername;
        request.password = mPassword;

        Call<LoginResponse> call = service.login(request);
        Response<LoginResponse> response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        if (response != null) {
            if (response.isSuccess()) {
                LoginResponse loginResponse = response.body();
                if (loginResponse.error) {
                    ErrorCode.Code code = ErrorCode.getCode(loginResponse.code);
                    ErrorCode errorCode = new ErrorCode(code);

                    mListener.loginFailed(errorCode);
                } else {
                    User user = new User();
                    user.setId((long) loginResponse.id);
                    user.setFirstName(loginResponse.first_name);
                    user.setLastName(loginResponse.last_name);
                    user.setUsername(loginResponse.username);
                    user.setDob(loginResponse.dob);
                    user.setEmail(loginResponse.email);
                    user.setIsModerator(loginResponse.is_moderator);

                    mListener.loginSuccessful(user);
                }
            } else {
                ErrorCode errorCode = new ErrorCode(ErrorCode.Code.GENERIC_ERROR);

                mListener.loginFailed(errorCode);
            }
        }

        return null;
    }

    @Override
    protected void onCancelled() {
        mListener.loginCanceled();
    }


}
