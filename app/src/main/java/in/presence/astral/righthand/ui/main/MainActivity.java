package in.presence.astral.righthand.ui.main;

import android.content.Context;
import android.content.Intent;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.InetAddress;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import in.presence.astral.righthand.R;
import in.presence.astral.righthand.model.UserObject;
import in.presence.astral.righthand.room.Control;
import in.presence.astral.righthand.service.DbSyncService;
import in.presence.astral.righthand.ui.LoginActivity;
import in.presence.astral.righthand.ui.roomcontrol.RoomControlFragment;
import in.presence.astral.righthand.ui.roomcontrol.RoomControlViewModel;
import timber.log.Timber;


public class MainActivity extends AppCompatActivity implements RoomControlFragment.OnFragmentInteractionListener {

    MqttAndroidClient mqttAndroidClient;


    public final String TAG = "NSD";
    public final String SERVICE_TYPE = "_righthand._tcp.";
    public final String mServiceName = "rhserver";

    NsdManager mNsdManager;
    NsdManager.DiscoveryListener mDiscoveryListener;
    RoomControlFragment mRoomControlFragment;
    MainViewModel mainViewModel;
    Toolbar toolbar;
    int grpCount=0,roomCount;

    NsdManager.ResolveListener mResolveListener;
    Drawer drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mRoomControlFragment = RoomControlFragment.newInstance();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, mRoomControlFragment)
                    .commitNow();
        }


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        initializeDiscoveryListener();

        UserObject user = new UserObject(this);
        String rt = user.getRefreshToken();
        if(rt==null||rt.isEmpty()){
            startActivity(new Intent(this,LoginActivity.class));
        }

        syncDB();


        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.app_name);



        //create the drawer and remember the `Drawer` result object
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D

                        if(drawerItem instanceof PrimaryDrawerItem) {

                            return false;
                        } else {

                            PrimaryDrawerItem primaryDrawerItem = (PrimaryDrawerItem) drawerItem.getParent();

                            String groupRoom = primaryDrawerItem.getName().getText(MainActivity.this) + "/" +
                                    ((SecondaryDrawerItem)drawerItem).getName().getText(MainActivity.this);
                            mainViewModel.setSelectedGroupRoom(groupRoom);
                        }
                        return true;
                    }
                })
                .build();


        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getDistinctGroups().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable final List<String> groups) {
                // Update the cached copy of the words in the adapter.

                grpCount=0;
                drawer.removeAllItems();

                for(String group: groups){

                    grpCount++;
                    PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(grpCount).withName(group);

                    mainViewModel.getDistinctRooms(group).observe(MainActivity.this, new Observer<List<String>>() {
                        @Override
                        public void onChanged(List<String> rooms) {

                            for(String room : rooms){
                                SecondaryDrawerItem item = new SecondaryDrawerItem().withName(room);
                                item1.withSubItems(item);
                            }
                        }
                    });
                    drawer.addItem(item1);
                }
            }
        });
    }

    public void syncDB(){

        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder()
                .setService(DbSyncService.class) // the JobService that will be called
                .setTag("my-unique-tag")        // uniquely identifies the job
                .build();

        dispatcher.mustSchedule(myJob);
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

                if(user.getRefreshToken()==null||user.getRefreshToken().isEmpty()){
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_signout) {
            signOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
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

        UserObject userObject = new UserObject(this);
        userObject.updateTokens(null,null,this);
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
    }
}
