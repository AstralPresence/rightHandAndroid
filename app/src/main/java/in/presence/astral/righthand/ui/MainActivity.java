package in.presence.astral.righthand.ui;

import android.content.Context;
import android.content.Intent;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.InetAddress;

import androidx.appcompat.app.AppCompatActivity;
import in.presence.astral.righthand.R;
import in.presence.astral.righthand.model.UserObject;
import in.presence.astral.righthand.ui.roomcontrol.RoomControlFragment;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements RoomControlFragment.OnFragmentInteractionListener {

    MqttAndroidClient mqttAndroidClient;


    public final String TAG = "NSD";
    public final String SERVICE_TYPE = "_righthand._tcp.";
    public final String mServiceName = "rhserver";

    NsdManager mNsdManager;
    NsdManager.DiscoveryListener mDiscoveryListener;

    NsdManager.ResolveListener mResolveListener ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, RoomControlFragment.newInstance())
                    .commitNow();
        }


        initializeDiscoveryListener();

        UserObject user = new UserObject(this);
        String rt = user.getRefreshToken();
        if(rt==null||rt.isEmpty()){
            startActivity(new Intent(this,LoginActivity.class));
        }



    }

    public void connectMqtt(String serverUri){

        String clientId = "Android" + System.currentTimeMillis();
        mqttAndroidClient = new MqttAndroidClient(getApplicationContext(), serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

                if (reconnect) {
                    // Because Clean Session is true, we need to re-subscribe
                    subscribeToTopics();
                }
            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                String[] groupRoomName= topic.split("/");



            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);

    }

    public void subscribeToTopics(){

    }

    public void initializeDiscoveryListener() {

        // Instantiate a new

        mNsdManager = (NsdManager)this.getSystemService(Context.NSD_SERVICE);
        mResolveListener= new NsdManager.ResolveListener() {

            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Called when the resolve fails. Use the error code to debug.
                Log.e(TAG, "Resolve failed: " + errorCode);
            }

            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                Log.e(TAG, "Resolve Succeeded. " + serviceInfo);


                UserObject user = new UserObject(getBaseContext());

                InetAddress host = serviceInfo.getHost();
                Timber.e("resolved %s resolved",host.toString());
                user.setServerIP(host.toString(),getBaseContext());

                if(user.getRefreshToken().isEmpty()||user.getRefreshToken()==null){
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                } else {
                    connectMqtt("tcp://" + host + ":1883");
                }

                if (serviceInfo.getServiceName().equals(mServiceName)) {
                    Log.d(TAG, "Same IP.");
                    return;
                }

            }
        };

        mDiscoveryListener = new NsdManager.DiscoveryListener() {

            // Called as soon as service discovery begins.
            @Override
            public void onDiscoveryStarted(String regType) {
                Log.d(TAG, "Service discovery started");
            }

            @Override
            public void onServiceFound(NsdServiceInfo service) {
                // A service was found! Do something with it.\
                Timber.i("service %s %s",service.getServiceType(),service.getServiceName());
                Log.d(TAG, "Service discovery success" + service);
                if (!service.getServiceType().equals(SERVICE_TYPE)) {
                    // Service type is the string containing the protocol and
                    // transport layer for this service.
                    Log.d(TAG, "Unknown Service Type: " + service.getServiceType());
                } else if (service.getServiceName().contains("rhserver")){
                    mNsdManager.resolveService(service, mResolveListener);
                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo service) {
                // When the network service is no longer available.
                // Internal bookkeeping code goes here.
                Log.e(TAG, "service lost: " + service);
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.i(TAG, "Discovery stopped: " + serviceType);
            }

            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                mNsdManager.stopServiceDiscovery(this);
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                mNsdManager.stopServiceDiscovery(this);
            }
        };
        mNsdManager.discoverServices(
                SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
    }

    @Override
    public void onUserControlEvent(String topic, int value) {

    }

    @Subscribe(threadMode= ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event){

        Timber.i(event.getMessage());

        switch (event.getMessage()){
            case "ServerUnreachable": {

                Toast.makeText(this, R.string.server_unreachable_msg,Toast.LENGTH_LONG).show();
                break;

            }
            case "TokenUpdateRefused":{

                signOut();
                Toast.makeText(this, R.string.requestlogin,Toast.LENGTH_LONG).show();
                break;
            }
            default:{
                Toast.makeText(this,event.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }

    public static class MessageEvent{
         private String message;

        public MessageEvent(String message){
            this.message=message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void signOut(){

    }
}
