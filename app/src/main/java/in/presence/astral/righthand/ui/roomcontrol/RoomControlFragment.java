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


    private OnFragmentInteractionListener mListener;

    public static RoomControlFragment newInstance() {
        return new RoomControlFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.room_control_fragment, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerview);
        adapter = new ControlsAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));



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
        mViewModel = ViewModelProviders.of(this).get(RoomControlViewModel.class);
        mViewModel.getRoomControls(group,room);/*.observe(this, new Observer<List<Control>>() {
            @Override
            public void onChanged(@Nullable final List<Control> ctrls) {
                // Update the cached copy of the words in the adapter.
                adapter.setControls(ctrls);
            }
        });*/

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onUserControlEvent(String topic, int value);
    }
}
