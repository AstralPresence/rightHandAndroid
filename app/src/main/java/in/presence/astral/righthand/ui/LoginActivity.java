package in.presence.astral.righthand.ui;

import android.os.Bundle;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.appcompat.app.AppCompatActivity;
import in.presence.astral.righthand.R;
import timber.log.Timber;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


    }


    @Subscribe(threadMode= ThreadMode.MAIN)
    public void onMessageEvent(MainActivity.MessageEvent event){

        Timber.i(event.getMessage());

        switch (event.getMessage()){
            case "ServerUnreachable": {

                Toast.makeText(this, R.string.server_unreachable_msg,Toast.LENGTH_LONG).show();
                break;

            }
            case "TokenUpdateRefused":{

                Toast.makeText(this, R.string.requestlogin,Toast.LENGTH_LONG).show();
                break;
            }
            default:{
                Toast.makeText(this,event.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
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

}
