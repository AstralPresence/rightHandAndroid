package in.presence.astral.righthand.service;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import in.presence.astral.righthand.model.Control;
import in.presence.astral.righthand.model.ControlsResponse;
import in.presence.astral.righthand.model.UserObject;
import in.presence.astral.righthand.rest.ApiClient;
import in.presence.astral.righthand.rest.ApiInterface;
import in.presence.astral.righthand.room.AppDatabase;
import in.presence.astral.righthand.ui.main.MainActivity;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by DevOpsTrends on 7/2/2017.
 */

public class DBSyncTask {

    private static Context mContext;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    static UserObject user;
    static int percentage, attended;

    synchronized public static void syncData(Context context) {

        mContext=context;
        user = new UserObject(mContext);

        fetchAccessToken();
        handleActionGetControls();


    }

    private static void fetchAccessToken() {

        UserObject user = new UserObject(mContext);
        String refreshToken = user.getRefreshToken();
        Timber.i("sending refresh token %s to server",refreshToken);

        try {

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(30, TimeUnit.SECONDS);
            builder.readTimeout(30, TimeUnit.SECONDS);
            builder.writeTimeout(30, TimeUnit.SECONDS);
            OkHttpClient client = builder.build();

            JSONObject postJSON = new JSONObject();
            postJSON.put("refreshToken",refreshToken);
            RequestBody body = RequestBody.create(JSON, String.valueOf(postJSON));
            Request request = new Request.Builder()
                    .url(new URL(ApiClient.BASE_URL+"getAccessToken"))
                    .post(body)
                    .build();


            okhttp3.Response response = client.newCall(request).execute();

            if(response.isSuccessful()){

                String responseString = response.body().string();
                Timber.i("AccessToken Response is %s",responseString);

                if(responseString.equals("fail")){

                    EventBus.getDefault().post(new MainActivity.MessageEvent("TokenUpdateRefused"));

                } else {

                    //registration, received refresh token
                    user.updateTokens(user.getRefreshToken(),responseString,mContext);

                }

            } else {

                EventBus.getDefault().post(new MainActivity.MessageEvent("ServerUnreachable"));

            }

        } catch (IOException | JSONException e1) {
            e1.printStackTrace();
            EventBus.getDefault().post(new MainActivity.MessageEvent("ServerUnreachable"));
        }
    }


    private  static void  handleActionGetControls() {

        user = new UserObject(mContext);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ControlsResponse> callTT = apiService.getControls(user.getAccessToken());

        try {
            Response<ControlsResponse> response = callTT.execute();
            if(response.isSuccessful()){
                if(response.code()==200){
                    List<Control> ctrlList = response.body().getMessage();
                    insertControlsList(ctrlList);
                } else if (response.code() == 401) { //bad auth
                    user.setAccessToken(null, mContext); //reset access token
                    fetchAccessToken();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void insertControlsList(List<Control> ctrlList) {


        for(Control ctrl: ctrlList){

            AppDatabase.getDatabase(mContext).controlObjectDao().insert(new in.presence.astral.righthand.room.Control(
                    ctrl.getGroup(),ctrl.getRoom(),ctrl.getName(),ctrl.getType(),ctrl.getDisplayName(),ctrl.getStatus()
            ));


        }

    }

}
