package in.presence.astral.righthand.ui.door;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import in.presence.astral.righthand.room.Control;
import in.presence.astral.righthand.room.ControlRepository;
import in.presence.astral.righthand.room.ModeRepository;

public class DoorViewModel extends AndroidViewModel {

    private ControlRepository mRepository;

    private MutableLiveData<String> mSelectedRoom;

    public DoorViewModel(Application application) {
        super(application);
        mRepository = new ControlRepository(application);

    }
    LiveData<List<String>> getDistinctGroups() {return  mRepository.getDistinctGroups(); }

    LiveData<List<String>> getDistinctRooms(String group) {return  mRepository.getAllRoomsOfGroup(group); }

    LiveData<List<Control>> getRoomControls(String group, String room) {
        return mRepository.getRoomControls(group,room);
    }

    LiveData<List<Control>> getRoomCameras(String group, String room) {
        return mRepository.getRoomCameras(group,room);
    }


    LiveData<List<Control>> getRoomLocks(String group, String room) {
        return mRepository.getRoomLocks(group,room);
    }


    public void insert(Control control) { mRepository.insertControls(control); }
}
