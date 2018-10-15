package in.presence.astral.righthand.ui.roomcontrol;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    public void onRoomSelected(String room, String group){


    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onUserControlEvent(String topic, int value);
    }
}
