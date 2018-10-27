package in.presence.astral.righthand.ui.roomcontrol;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.presence.astral.righthand.R;
import in.presence.astral.righthand.adapter.ControlsAdapter;
import in.presence.astral.righthand.room.Control;
import in.presence.astral.righthand.ui.LoginActivity;
import in.presence.astral.righthand.ui.main.MainActivity;
import in.presence.astral.righthand.ui.main.MainViewModel;
import timber.log.Timber;

public class RoomControlFragment extends Fragment {

    private RoomControlViewModel mViewModel;
    ControlsAdapter adapter;
    View rootView;
    RecyclerView controlsRecyclerView;
    static String group, room;


    private OnFragmentInteractionListener mListener;

    public static RoomControlFragment newInstance() {
        return new RoomControlFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Timber.i("fragment creation");
        rootView =  inflater.inflate(R.layout.room_control_fragment, container, false);
        controlsRecyclerView = rootView.findViewById(R.id.roomcontrolsrecyclerview);
        adapter = new ControlsAdapter(getContext());
        controlsRecyclerView.setAdapter(adapter);
        controlsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return rootView;

    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onUserControlEvent(String topic, int value);
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

        String[] gr = event.getMessage().split("/");


        mViewModel = ViewModelProviders.of(this).get(RoomControlViewModel.class);
        mViewModel.getRoomControls(gr[0],gr[1]).observe(RoomControlFragment.this, new Observer<List<Control>>() {
            @Override
            public void onChanged(List<Control> controls) {
                adapter.setControls(controls);
            }
        });

    }

}
