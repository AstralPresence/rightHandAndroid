package in.presence.astral.righthand.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.IntentSender;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.IOException;

import in.presence.astral.righthand.rest.ApiClient;
import in.presence.astral.righthand.rest.ApiInterface;
import in.presence.astral.righthand.ui.door.DoorActivity;
import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

public class SendRTSPSignalService extends IntentService {
    public SendRTSPSignalService(String name) {
        super(name);
    }
    public SendRTSPSignalService() {
        super("SendRTSPSignalService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<JSONObject> callTT = apiService.playSpeaker();
        Response<JSONObject> response = null;
        try {
            response = callTT.execute();
            if(response.isSuccessful()){

                Timber.i("Response successful started mic");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
