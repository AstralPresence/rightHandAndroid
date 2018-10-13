package in.presence.astral.righthand.ui.roomcontrol;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import in.presence.astral.righthand.room.Control;
import in.presence.astral.righthand.room.ControlRepository;

public class RoomControlViewModel extends AndroidViewModel {

    private ControlRepository mRepository;

    private LiveData<List<Control>> mAllControls;

    public RoomControlViewModel (Application application) {
        super(application);
        mRepository = new ControlRepository(application);
        mAllControls = mRepository.getAllControls();
    }

    LiveData<List<Control>> getAllControls() { return mAllControls; }


    LiveData<List<Control>> getRoomControls(String group, String room) {
        return mRepository.getRoomControls(group,room);
    }


    public void insert(Control control) { mRepository.insertControls(control); }
}
