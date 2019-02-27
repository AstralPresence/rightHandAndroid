package in.presence.astral.righthand.ui.door;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import in.presence.astral.righthand.R;
import in.presence.astral.righthand.room.Control;
import in.presence.astral.righthand.service.SendRTSPSignalService;
import in.presence.astral.righthand.ui.main.MainActivity;
import in.presence.astral.righthand.ui.roomcontrol.RoomControlFragment;
import in.presence.astral.righthand.ui.roomcontrol.RoomControlViewModel;
import timber.log.Timber;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pedro.rtplibrary.rtsp.RtspCamera1;
import com.pedro.rtplibrary.rtsp.RtspOnlyAudio;
import com.pedro.rtsp.utils.ConnectCheckerRtsp;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class DoorActivity extends AppCompatActivity implements ConnectCheckerRtsp {

    private DoorViewModel mViewModel;
    FloatingActionButton mic;
    Boolean locked = true;

    int notificationId = 00125;

    VideoView videoView;

    TextView lockedStatus;

    String group, room;
    RtspOnlyAudio rtspAudio;
    ImageView lockImage;

    String CHANNEL_ID = "alarm";

    public final int AUDIO_RECORD_PERMISSION_REQUEST_CODE = 123;

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_door);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        group = getIntent().getStringExtra("group");
        room = getIntent().getStringExtra("room");

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        videoView = findViewById(R.id.video_player_view);
        lockImage= findViewById(R.id.lockImage);
        lockedStatus = findViewById(R.id.lockedStatus);


        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "righthand"));
        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        mic = findViewById(R.id.mic);


        mViewModel = ViewModelProviders.of(this).get(DoorViewModel.class);
        mViewModel.getRoomCameras(group,room).observe(DoorActivity.this, new Observer<List<Control>>() {
            @Override
            public void onChanged(List<Control> controls) {

                if(controls.size()>0){/*
                    MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                            .createMediaSource(Uri.parse("http://"+controls.get(0).getIp()+":8080"));*/
                    videoView.setVideoPath("http://192.168.1.109:8080/hls/index.m3u8");
                    videoView.start();



                    mic.setOnTouchListener((view, motionEvent) -> {
                        Timber.i("motion event %d ",motionEvent.getAction());
                        switch (motionEvent.getAction()){
                            case 0: { //down

                                checkRecordPermission();
                                if(arePermissionsGranted()){
                                    rtspAudio = new RtspOnlyAudio(DoorActivity.this);
                                    //start stream
                                    if (rtspAudio.prepareAudio()) {

                                        Toast.makeText(DoorActivity.this,"Starting stream ...",Toast.LENGTH_LONG).show();
                                        //rtspAudio.startStream("rtsp://"+controls.get(0).getIp()+":80/righthand/mic");
                                        rtspAudio.startStream("rtsp://192.168.1.109:80/righthand/mic");
                                        startService(new Intent(DoorActivity.this, SendRTSPSignalService.class));
                                    } else {
                                        /**This device cant init encoders, this could be for 2 reasons: The encoder selected doesnt support any configuration setted or your device hasnt a H264 or AAC encoder (in this case you can see log error valid encoder not found)*/
                                        Timber.e("could not prepare audiovideo");
                                    }
                                } else {

                                    Toast.makeText(DoorActivity.this,"Please grant permissions",Toast.LENGTH_LONG).show();
                                }


                                break;
                            }
                            case 1:{ //up
                                //stop stream
                                if(rtspAudio!=null){
                                    rtspAudio.stopStream();
                                    Toast.makeText(DoorActivity.this,"Stopping live stream.",Toast.LENGTH_LONG).show();
                                }
                            }
                            default:{
                                break;
                            }
                        }
                        return true;
                    });
                }
            }

        });
        mViewModel.getRoomLocks(group,room).observe(DoorActivity.this, new Observer<List<Control>>() {
            @Override
            public void onChanged(List<Control> controls) {

                if(controls.size()>0){
                    lockedStatus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Timber.i("sending open");
                            lockedStatus.setBackgroundColor(getResources().getColor(R.color.red));
                            lockedStatus.setText("UNLOCKED");
                            lockImage.setImageResource(R.drawable.ic_lock_open);
                            locked=false;
                            EventBus.getDefault().post(new MainActivity.MessageEvent("mqtt",controls.get(0).getName(),"open"));


                        }
                    });


                }
            }
        });


        Toast.makeText(DoorActivity.this,"Touch and hold the mic to speak",Toast.LENGTH_SHORT).show();
    }

    private void checkRecordPermission() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                    AUDIO_RECORD_PERMISSION_REQUEST_CODE);
        }


    }

    public boolean arePermissionsGranted(){
        boolean audioP = ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)== PackageManager.PERMISSION_GRANTED;

        return  audioP;

    }

    @Override
    public void onConnectionSuccessRtsp() {
        Timber.i("RTSP connection success");
    }

    @Override
    public void onConnectionFailedRtsp(String reason) {

        Timber.i("RTSP connection failed");
    }

    @Override
    public void onDisconnectRtsp() {

        Timber.i("RTSP disconnected");
    }

    @Override
    public void onAuthErrorRtsp() {

        Timber.i("RTSP auth error");
    }

    @Override
    public void onAuthSuccessRtsp() {

        Timber.i("RTSP auth success");
    }

    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


    public static class MessageEvent{
        private String topic, doorState;

        public MessageEvent(String topic, String doorState){
            this.topic=topic;
            this.doorState=doorState;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public String getDoorState() {
            return doorState;
        }

        public void setDoorState(String doorState) {
            this.doorState = doorState;
        }
    }

    @Subscribe(threadMode= ThreadMode.MAIN)
    public void onMessageEvent(DoorActivity.MessageEvent event){

        Timber.i(event.getDoorState()+event.getTopic());

        if(event.getDoorState().equals("closed")){

            locked=true;
            lockedStatus.setBackgroundColor(getResources().getColor(R.color.green));
            lockedStatus.setText("LOCKED");
            lockImage.setImageResource(R.drawable.ic_lock_open);

        } else if (event.getDoorState().equals("opened")){
            locked=false;

            lockedStatus.setBackgroundColor(getResources().getColor(R.color.red));
            lockedStatus.setText("UNLOCKED\nplease close the door");
            lockImage.setImageResource(R.drawable.ic_lock_open);
        }else if (event.getDoorState().equals("break-In")){

            createNotification();


            lockedStatus.setBackgroundColor(getResources().getColor(R.color.red));
            lockedStatus.setText("EMERGENCY!\nBREAK-IN DETECTED");
            lockImage.setImageResource(R.drawable.ic_lock_open);
        }



    }
    private void createNotification() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.alarm_channel_name);
            String description = getString(R.string.alarm_channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        Intent intent = new Intent(this, DoorActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Break In Detected!!")
                .setContentText("Attention! Break In Detected")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Attention! Break In Detected"))
                .setContentIntent(pendingIntent)
                .setSound(soundUri)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId, builder.build());
    }
}
