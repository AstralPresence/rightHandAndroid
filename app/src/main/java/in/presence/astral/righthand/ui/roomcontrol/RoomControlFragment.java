package in.presence.astral.righthand.ui.roomcontrol;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.presence.astral.righthand.R;
import in.presence.astral.righthand.adapter.ControlsAdapter;
import in.presence.astral.righthand.room.Control;
import in.presence.astral.righthand.ui.door.DoorActivity;
import timber.log.Timber;

public class RoomControlFragment extends Fragment {

    private RoomControlViewModel mViewModel;
    ControlsAdapter adapter;
    View rootView;
    RecyclerView controlsRecyclerView;
    FloatingActionButton camFab, hotFab;
    String[] gr;

    AlertDialog.Builder builderSingle;


    public static RoomControlFragment newInstance() {
        return new RoomControlFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Timber.i("fragment creation");
        rootView =  inflater.inflate(R.layout.room_control_fragment, container, false);
        camFab = rootView.findViewById(R.id.camMicFAB);
        hotFab = rootView.findViewById(R.id.whatsHot);
        controlsRecyclerView = rootView.findViewById(R.id.roomcontrolsrecyclerview);
        adapter = new ControlsAdapter(getContext());
        controlsRecyclerView.setAdapter(adapter);
        controlsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        builderSingle = new AlertDialog.Builder(getActivity());
        builderSingle.setIcon(R.mipmap.ic_launcher);
        builderSingle.setTitle("Select A Mode");


        final ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_selectable_list_item);


        mViewModel = ViewModelProviders.of(this).get(RoomControlViewModel.class);

        mViewModel.getAllModes().observe(RoomControlFragment.this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> modes) {

                if(modes.size()>0){

                    hotFab.show();

                    Timber.i("on changed called");

                    modeAdapter.clear();

                    for(String md : modes){
                        modeAdapter.add(md);
                    }

                    modeAdapter.add(" + New Mode");

                } else{
                    hotFab.hide();
                }
            }
        });
        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(modeAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String modeName = modeAdapter.getItem(which);

                if(modeName.equals(" + New Mode")){

                    SaveModeDialogClass saveModeDialogClass = new SaveModeDialogClass(getActivity());
                    WindowManager.LayoutParams lWindowParams = new WindowManager.LayoutParams();
                    lWindowParams.copyFrom(saveModeDialogClass.getWindow().getAttributes());
                    lWindowParams.width = WindowManager.LayoutParams.FILL_PARENT; // this is where the magic happens
                    lWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    saveModeDialogClass.show();// I was told to call show first I am not sure if this it to cause layout to happen so that we can override width?
                    saveModeDialogClass.getWindow().setAttributes(lWindowParams);
                } else {
                    Toast.makeText(getActivity(),"Setting mode "+ modeName,Toast.LENGTH_LONG).show();
                }

            }
        });
        hotFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builderSingle.show();

            }
        });
        return rootView;

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

    @Subscribe(threadMode= ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event){

        Timber.i(event.getMessage());

        if(!event.getMessage().contains("reload")){
            gr = event.getMessage().split("/");

            mViewModel = ViewModelProviders.of(this).get(RoomControlViewModel.class);

        }
        mViewModel.getRoomControls(gr[0],gr[1]).observe(RoomControlFragment.this, new Observer<List<Control>>() {
            @Override
            public void onChanged(List<Control> controls) {

                Timber.i("on changed called");
                adapter.setControls(controls);
            }
        });
        mViewModel.getRoomCameras(gr[0],gr[1]).observe(RoomControlFragment.this, new Observer<List<Control>>() {
            @Override
            public void onChanged(List<Control> controls) {

                if(controls.size()>0){
                    camFab.show();
                    camFab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), DoorActivity.class);
                            intent.putExtra("group",gr[0]);
                            intent.putExtra("room",gr[1]);
                            startActivity(intent);
                        }
                    });
                } else {
                    camFab.hide();
                }
            }
        });

    }



    public class SaveModeDialogClass extends Dialog implements
            android.view.View.OnClickListener {

        public Activity c;
        EditText modeName;
        public Button saveMode;
        public RadioGroup radioGroup;

        public SaveModeDialogClass(Activity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.save_mode_dialog);

            radioGroup = findViewById(R.id.radioButtons);

            modeName = findViewById(R.id.modeName);
            saveMode = findViewById(R.id.createMode);
            saveMode.setOnClickListener(this);
        }

        @Override
        public void onClick(View v){

            if(radioGroup.getCheckedRadioButtonId()==R.id.radioGroup){

                Toast.makeText(getActivity(),"Saving group state",Toast.LENGTH_LONG).show();
            } else {

                Toast.makeText(getActivity(),"Saving room state",Toast.LENGTH_LONG).show();

            }

        }
    }

}
