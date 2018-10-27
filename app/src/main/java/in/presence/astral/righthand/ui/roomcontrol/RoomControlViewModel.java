package in.presence.astral.righthand.ui.roomcontrol;

import android.app.Application;

import org.w3c.dom.ls.LSException;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import in.presence.astral.righthand.room.Control;
import in.presence.astral.righthand.room.ControlRepository;

public class RoomControlViewModel extends AndroidViewModel {

    private ControlRepository mRepository;

    private MutableLiveData<String> mSelectedRoom;

    public RoomControlViewModel (Application application) {
        super(application);
        mRepository = new ControlRepository(application);
        mSelectedRoom = mRepository.getSelectedGroupRoom();
    }


    LiveData<List<String>> getDistinctGroups() {return  mRepository.getDistinctGroups(); }

    LiveData<List<String>> getDistinctRooms(String group) {return  mRepository.getAllRoomsOfGroup(group); }

    LiveData<List<Control>> getRoomControls(String group, String room) {
        return mRepository.getRoomControls(group,room);
    }


    public void insert(Control control) { mRepository.insertControls(control); }
}
