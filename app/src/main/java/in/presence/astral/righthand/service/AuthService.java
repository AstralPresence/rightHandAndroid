package in.presence.astral.righthand.service;

import android.app.IntentService;
import android.content.Intent;


import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import in.presence.astral.righthand.model.UserObject;
import in.presence.astral.righthand.rest.ApiClient;
import in.presence.astral.righthand.ui.LoginActivity;
import in.presence.astral.righthand.ui.MainActivity;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import timber.log.Timber;


public class AuthService extends IntentService {

    public static final String ACTION_LOGIN = "com.apptronix.nitkonschedule.service.action.LOGIN";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public AuthService() {
        super("AuthService");
    }

    private String token, responseString, message;

    @Override
    protected void onHandleIntent(Intent intent) {

        if(intent.getAction().equals(ACTION_LOGIN)){
            //fetch refresh token
            token = intent.getStringExtra("idToken");
            handleActionLogin();
        } else {
            //fetch access token
            UserObject user = new UserObject(this);
            if(user.getRefreshToken()==null) { // user has logged out, route to login page
                EventBus.getDefault().post(new MainActivity.MessageEvent("NoRefreshToken"));
            }
        }
    }

    private void handleActionLogin() {
/*


        Timber.i("sending token %s to server",token);
        User user = new User(this);
        Login loginBody = new Login(token,User.getFcmID());
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<String> callTT = apiService.login(loginBody);
        Response<String> response = callTT.execute();
        if(response.isSuccessful()){
            if(response.code()==200){

                Timber.i("RefreshToken Responese %s",response.body());
                if(!response.body().equals("fail")){
                    message="LoginFailed";
                } else {
                    message="LoginSuccessful";
                    User.updateTokens(response.body(),null,this);
                }


            } else { //bad refresh token
                message="LoginFailed";
            }
        } else {

            EventBus.getDefault().post(new MainActivity.MessageEvent("ServerUnreachable"));
        }
*/

        try {
            Timber.i("sending token %s to server",token);

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(30, TimeUnit.SECONDS);
            builder.readTimeout(30, TimeUnit.SECONDS);
            builder.writeTimeout(30, TimeUnit.SECONDS);
            OkHttpClient client = builder.build();

            UserObject user = new UserObject(this);
            JSONObject postJSON = new JSONObject();
            postJSON.put("idToken",token);
            postJSON.put("fcmID",user.getFcmID());
            RequestBody body = RequestBody.create(JSON, String.valueOf(postJSON));
            Request request = new Request.Builder()
                    .url(new URL(ApiClient.BASE_URL+"login"))
                    .post(body)
                    .build();

            //for emulator "http://10.0.2.2:5000/login"
            okhttp3.Response response = client.newCall(request).execute();

            if(response.isSuccessful()){

                responseString = response.body().string();
                Timber.i("RefreshToken Responese %s",responseString);

                if(responseString.equals("fail")){

                    message="LoginFailed";

                } else {

                    //registration, received refresh token
                    user.updateTokens(responseString,null,this);
                    message="LoginSuccessful";

                }

            } else {

                message="ServerUnreachable";

            }


        } catch (IOException | JSONException e1) {
            e1.printStackTrace();

            message="ServerUnreachable";
        }

        EventBus.getDefault().post(new LoginActivity.LoginEvent(message));
    }

}
