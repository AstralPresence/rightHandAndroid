package in.presence.astral.righthand.ui.door;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import in.presence.astral.righthand.R;
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

public class DoorActivity extends AppCompatActivity implements ConnectCheckerRtsp {

    FloatingActionButton mic;
    SimpleExoPlayerView simpleExoPlayerView;
    SimpleExoPlayer player;
    TextView lockedStatus;
    String group, room;
    RtspOnlyAudio rtspAudio;

    @Override
    protected void onPause() {

        player.release();
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_door);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        simpleExoPlayerView = findViewById(R.id.exo_player_view);

        player = ExoPlayerFactory.newSimpleInstance(this);

        simpleExoPlayerView.setPlayer(player);

        player.setPlayWhenReady(true);

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "righthand"));
        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"));
        player.prepare(videoSource);

        mic = findViewById(R.id.mic);
        group = getIntent().getStringExtra("group");
        room = getIntent().getStringExtra("room");



        mic.setOnTouchListener((view, motionEvent) -> {
            Timber.i("motion event %d ",motionEvent.getAction());
            switch (motionEvent.getAction()){
                case 0: { //down

                    checkRecordPermission();
                    if(arePermissionsGranted()){
                        rtspAudio = new RtspOnlyAudio(DoorActivity.this);
                        //start stream
                        if (rtspAudio.prepareAudio()) {
                            rtspAudio.startStream("rtsp://192.168.1.100:80/righthand/mic");
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

    private void checkRecordPermission() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                    123);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_SETTINGS},
                    124);
        }


    }

    public boolean arePermissionsGranted(){
        //return ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED;
        return true;
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
}
