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

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import in.presence.astral.righthand.R;
import in.presence.astral.righthand.model.User;
import in.presence.astral.righthand.model.UserObject;
import in.presence.astral.righthand.room.Control;
import in.presence.astral.righthand.service.DbSyncService;
import in.presence.astral.righthand.ui.LoginActivity;
import in.presence.astral.righthand.ui.door.DoorActivity;
import in.presence.astral.righthand.ui.users.UsersActivity;
import in.presence.astral.righthand.ui.roomcontrol.RoomControlFragment;
import timber.log.Timber;


public class MainActivity extends AppCompatActivity {

    MqttAndroidClient mqttAndroidClient;


    public final String TAG = "NSD";
    public final String SERVICE_TYPE = "_righthand._tcp.";
    public final String mServiceName = "rhserver";

    NsdManager mNsdManager;
    NsdManager.DiscoveryListener mDiscoveryListener;
    RoomControlFragment mRoomControlFragment;
    SecondaryDrawerItem item;
    MainViewModel mainViewModel;
    Toolbar toolbar;
    int grpCount=0,roomCount;

    NsdManager.ResolveListener mResolveListener;
    Drawer drawer;

    String selectedGroup,selectedRoom;

    @Override
    protected void onResume() {

        UserObject user = new UserObject(MainActivity.this);
        String rt = user.getRefreshToken();
        if(rt==null||rt.isEmpty()){
            startActivity(new Intent(this,LoginActivity.class));
        } else{
            syncDB();
        }
        super.onResume();
    }


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

        EventBus.getDefault().register(this);
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




        try {
            connectMqtt("tcp://192.168.1.109:1883");
        } catch (MqttException e) {
            e.printStackTrace();
        }
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

                            selectedGroup = primaryDrawerItem.getName().getText(MainActivity.this);
                            selectedRoom = ((SecondaryDrawerItem)drawerItem).getName().getText(MainActivity.this);

                            String groupRoom = selectedGroup + "/" +selectedRoom;
                            Timber.i("ccliclkd %s",groupRoom);
                            EventBus.getDefault().post(new RoomControlFragment.MessageEvent(groupRoom));

                            drawer.closeDrawer();
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

                PrimaryDrawerItem itemP = new PrimaryDrawerItem().withIdentifier(1).withSelectable(false).withName("--Rooms--");
                drawer.addItem(itemP);

                for(String group: groups){

                    Timber.i("parsing menu group %s",group);

                    grpCount++;
                    PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(grpCount).withSelectable(false).withName(group);

                    mainViewModel.getDistinctRooms(group).observe(MainActivity.this, new Observer<List<String>>() {
                        @Override
                        public void onChanged(List<String> rooms) {

                            for(String room : rooms){

                                Timber.i("parsing menu room %s",room);

                                if(room.toLowerCase().contains("kitchen")){
                                    item = new SecondaryDrawerItem().withName(room).withIcon(R.drawable.ic_kitchen_24dp);
                                } else if(room.toLowerCase().contains("living")){
                                    item = new SecondaryDrawerItem().withName(room).withIcon(R.drawable.ic_living_room_24dp);
                                } else if(room.toLowerCase().contains("bedroom")){
                                    item = new SecondaryDrawerItem().withName(room).withIcon(R.drawable.ic_bedroom_black_24dp);
                                }  else if(room.toLowerCase().contains("office")){
                                    item = new SecondaryDrawerItem().withName(room).withIcon(R.drawable.ic_work).withIsExpanded(true);
                                } else {
                                    item = new SecondaryDrawerItem().withName(room).withIcon(R.drawable.ic_plant_black_24dp);
                                }
                                item1.withSubItems(item);

                            }
                        }
                    });
                    drawer.addItem(item1);
                }
            }
        });

        drawer.getExpandableExtension().expand(1);
        drawer.getExpandableExtension().expand(2);
        drawer.getExpandableExtension().expand(0);
        drawer.getExpandableExtension().expand();
        drawer.openDrawer();
        drawer.getExpandableExtension().expand(1);
        drawer.getExpandableExtension().expand(2);
        drawer.getExpandableExtension().expand(0);
        drawer.getExpandableExtension().expand();
    }

    public void syncDB(){


        Intent intent = new Intent(this,DbSyncService.class);
        startService(intent);
    }


    public void connectMqtt(String serverUri) throws MqttException {

        if(null==serverUri||serverUri.isEmpty()){
            serverUri="tcp://192.168.1.109:1883";

        }
        Timber.i("mqtt attempt started");
        String clientId = MqttClient.generateClientId();
        mqttAndroidClient = new MqttAndroidClient(getApplicationContext(), serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

                Timber.i(" mqtt connection complete");
                if(reconnect){
                    subscribeToTopics();
                }
            }

            @Override
            public void connectionLost(Throwable cause) {

                Timber.i("lost mqtt connection");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {

                Timber.i("message arrived mqtt %s",new String(message.getPayload()));
                try {

                    JSONObject jsonObject = new JSONObject(new String(message.getPayload()));
                    Iterator<String> keys = jsonObject.keys();


                    while(keys.hasNext()){
                        String key = keys.next();
                        if(key.equals("state")){
                            int state = jsonObject.getInt("state");

                            Timber.i("revec %d",state);
                            MessageEvent messageEvent = new MessageEvent("dbUpdate",topic,state);
                            EventBus.getDefault().post(messageEvent);

                            EventBus.getDefault().post(new RoomControlFragment.MessageEvent("reload"));
                        } else if (key.equals("door")){

                            EventBus.getDefault().post(new DoorActivity.MessageEvent(topic,jsonObject.getString("door")));
                        }
                    }




                } catch (JSONException e) {
                    Timber.i("JSON invalid");
                    e.printStackTrace();
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });


        MqttConnectOptions options = new MqttConnectOptions();
        options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
        options.setAutomaticReconnect(true);
        options.setCleanSession(false);
        options.setKeepAliveInterval(5);

        mqttAndroidClient.connect(options, null, new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                disconnectedBufferOptions.setBufferEnabled(true);
                disconnectedBufferOptions.setBufferSize(100);
                disconnectedBufferOptions.setPersistBuffer(false);
                disconnectedBufferOptions.setDeleteOldestMessages(false);
                mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                subscribeToTopics();
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

            }
        });



    }


    public void subscribeToTopics(){

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getAllControls().observe(MainActivity.this, new Observer<List<Control>>() {
            @Override
            public void onChanged(List<Control> controls) {

                for (Control c: controls){
                    String group = c.getGroup();
                    String room = c.getRoom();
                    String name = c.getName();
                    Timber.i("mqtt subscribe %s",c.getName());
                    try {

                        mqttAndroidClient.subscribe(group + "/" + room + "/" + name, 0, null, new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {

                                Timber.i("topic subscribed");
                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                            }
                        });
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

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
                    try {
                        connectMqtt("tcp://" + host + ":1883");
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
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
                    Log.d(TAG, "Unknown Service Type : " + service.getServiceType());
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
        } else if (id == R.id.action_users) {
            startActivity(new Intent(this,UsersActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Subscribe(threadMode= ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event){

        Timber.i("main activity message received"+event.getMessage());

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
            case "mqtt":{

                String topic = selectedGroup+"/"+selectedRoom+"/"+event.getControlName();
                Timber.i("%s %s",topic,String.valueOf(event.getStatus()));
                JSONObject jsonObject = new JSONObject();
                if(event.getDoorMessage()==null){

                    try {
                        jsonObject.put("state",event.getStatus());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if(event.getDoorMessage().equals("open")) {
                    Timber.i("Sending door open");
                    try {
                        jsonObject.put("door",event.getDoorMessage());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Timber.i("sending json on mqtt "+jsonObject.toString() +" to topic "+topic);
                    MqttMessage message = new MqttMessage(jsonObject.toString().getBytes("UTF-8"));
                    message.setQos(2);
                    mqttAndroidClient.publish(topic,message);
                } catch (UnsupportedEncodingException | MqttException e) {
                    Timber.i(e.getMessage());
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this,"not connected",Toast.LENGTH_LONG).show();
                }
                break;
            }
            case "dbUpdate":{
                String[] grn= event.getControlName().split("/");

                mainViewModel.updateStatus(grn[0],grn[1],grn[2],event.status);
            }
            default:{
                Toast.makeText(this,event.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }

    public static class MessageEvent{
        private String message;

        private String controlName;

        private float status;

        public String doorMsg;

        public MessageEvent(String message){
            this.message=message;
        }

        public MessageEvent(String message,String controlName, float status){
            this.message=message;
            this.controlName=controlName;
            this.status=status;
        }

        public MessageEvent(String message,String controlName, String doorMsg){
            this.message=message;
            this.controlName=controlName;
            this.doorMsg=doorMsg;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }


        public String getDoorMessage() {
            return doorMsg;
        }

        public void setDoorMessage(String doorMessage) {
            this.doorMsg = doorMessage;
        }

        public String getControlName() {
            return controlName;
        }

        public void setControlName(String controlName) {
            this.controlName = controlName;
        }

        public float getStatus() {
            return status;
        }

        public void setStatus(float status) {
            this.status = status;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Timber.i("on start called");
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        Timber.i("on stop called");
        super.onDestroy();
    }



    private void signOut(){

        UserObject userObject = new UserObject(this);
        userObject.updateTokens(null,null,this);
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
    }
}
