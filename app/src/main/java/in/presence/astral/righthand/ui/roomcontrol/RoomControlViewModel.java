package in.presence.astral.righthand.ui.roomcontrol;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import in.presence.astral.righthand.room.Control;
import in.presence.astral.righthand.room.ControlRepository;

public class RoomControlViewModel extends AndroidViewModel {

    private ControlRepository mRepository;

    private LiveData<List<Control>> mAllControls;

    public RoomControlViewModel (Application application) {
        super(application);
        mRepository = new ControlRepository(application);
        mAllControls = mRepository.getAllWords();
    }

    LiveData<List<Control>> getAllControls() { return mAllControls; }



    public void insert(Control control) { mRepository.insert(control); }
}
