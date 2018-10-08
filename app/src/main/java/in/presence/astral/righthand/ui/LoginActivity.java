package in.presence.astral.righthand.ui;

import android.content.Context;
import android.content.Intent;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.InetAddress;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import in.presence.astral.righthand.R;
import in.presence.astral.righthand.model.UserObject;
import timber.log.Timber;

public class LoginActivity extends AppCompatActivity {

    TextView textDummyHintUsername;
    TextView textDummyHintPassword;
    EditText editUsername;
    AppCompatButton lgnBtn;
    EditText editPassword;


    public final String TAG = "NSD";
    public final String SERVICE_TYPE = "_righthand._tcp.";
    public final String mServiceName = "rhserver";

    NsdManager mNsdManager;
    NsdManager.DiscoveryListener mDiscoveryListener;

    NsdManager.ResolveListener mResolveListener ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();
        textDummyHintUsername = findViewById(R.id.text_dummy_hint_username);
        textDummyHintPassword = findViewById(R.id.text_dummy_hint_password);
        editUsername = findViewById(R.id.edit_username);
        editPassword = findViewById(R.id.edit_password);
        lgnBtn = findViewById(R.id.btn_lgn);

        // Username
        editUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            // Show white background behind floating label
                            textDummyHintUsername.setVisibility(View.VISIBLE);
                        }
                    }, 100);
                } else {
                    // Required to show/hide white background behind floating label during focus change
                    if (editUsername.getText().length() > 0)
                        textDummyHintUsername.setVisibility(View.VISIBLE);
                    else
                        textDummyHintUsername.setVisibility(View.INVISIBLE);
                }
            }
        });

        // Password
        editPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            // Show white background behind floating label
                            textDummyHintPassword.setVisibility(View.VISIBLE);
                        }
                    }, 100);
                } else {
                    // Required to show/hide white background behind floating label during focus change
                    if (editPassword.getText().length() > 0)
                        textDummyHintPassword.setVisibility(View.VISIBLE);
                    else
                        textDummyHintPassword.setVisibility(View.INVISIBLE);
                }
            }
        });

        lgnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validate()) {
                    Toast.makeText(LoginActivity.this,"Invalid crdentials",Toast.LENGTH_LONG).show();
                    return;
                }

                lgnBtn.setEnabled(false);


                String email = editUsername.getText().toString();
                String password = editPassword.getText().toString();

            }
        });

        initializeDiscoveryListener();

    }


    @Subscribe(threadMode= ThreadMode.MAIN)
    public void onMessageEvent(MainActivity.MessageEvent event){

        Timber.i(event.getMessage());

        switch (event.getMessage()){
            case "LoginSuccessful":{

                startActivity(new Intent(this,MainActivity.class));
                finish();
                break;

            } case "LoginFailed":{

                Toast.makeText(this, R.string.server_authentication_failed, Toast.LENGTH_LONG).show();
                break;

            }case "ServerUnreachable": {

                Toast.makeText(this, R.string.server_unreachable_msg,Toast.LENGTH_LONG).show();
                break;

            }

        }
    }


    public boolean validate() {
        boolean valid = true;

        String email = editUsername.getText().toString();
        String password = editPassword.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editUsername.setError(getResources().getString(R.string.invalid_email));
            valid = false;
        } else {
            editUsername.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            editPassword.setError(getResources().getString(R.string.password_error));
            valid = false;
        } else {
            editUsername.setError(null);
        }

        return valid;
    }


    public static class LoginEvent{
        private String message;

        public LoginEvent(String message){
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
}
