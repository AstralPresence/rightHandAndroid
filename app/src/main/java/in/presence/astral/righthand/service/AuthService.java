package in.presence.astral.righthand.service;

import android.app.IntentService;
import android.content.Intent;


import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.net.SocketTimeoutException;

import in.presence.astral.righthand.model.Login;
import in.presence.astral.righthand.model.UserObject;
import in.presence.astral.righthand.rest.ApiClient;
import in.presence.astral.righthand.rest.ApiInterface;
import in.presence.astral.righthand.ui.LoginActivity;
import in.presence.astral.righthand.ui.main.MainActivity;
import okhttp3.MediaType;
import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;


public class AuthService extends IntentService {

    public static final String ACTION_LOGIN = "in.presence.astral.righthand.service.action.LOGIN";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public AuthService() {
        super("AuthService");
    }

    private String email, password, responseString, message;

    @Override
    protected void onHandleIntent(Intent intent) {

        if(intent.getAction().equals(ACTION_LOGIN)){
            //fetch refresh token
            password = intent.getStringExtra("password");
            email = intent.getStringExtra("email");
            try {
                handleActionLogin();
            } catch (IOException e) {
                Timber.i("catching exception auth");
                EventBus.getDefault().post(new MainActivity.MessageEvent("Check internet connection"));
                e.printStackTrace();
            }
        } else {
            //fetch access token
            UserObject user = new UserObject(this);
            if(user.getRefreshToken()==null) { // user has logged out, route to login page
                EventBus.getDefault().post(new MainActivity.MessageEvent("NoRefreshToken"));
            }
        }
    }

    private void handleActionLogin() throws IOException {

        UserObject user = new UserObject(this);
        Login loginBody = new Login(email,password);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<String> callTT = apiService.login(loginBody);
        Response<String> response = callTT.execute();

        if(response.isSuccessful()){

            if(response.code()==200){

                Timber.i("RefreshToken Response %s",response.body());


                if(response.body().equals("fail")){

                    message="LoginFailed";

                } else {

                    message="LoginSuccessful";
                    user.updateTokens(response.body(),null,this);
                }


            } else { //bad refresh token
                message="LoginFailed";
            }

            EventBus.getDefault().post(new LoginActivity.MessageEvent(message));

        } else {

            EventBus.getDefault().post(new LoginActivity.MessageEvent("ServerUnreachable"));
        }

    }

}
