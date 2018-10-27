package in.presence.astral.righthand.ui.main;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import in.presence.astral.righthand.room.Control;
import in.presence.astral.righthand.room.ControlRepository;

public class MainViewModel extends AndroidViewModel {

    private ControlRepository mRepository;

    private LiveData<List<Control>> mAllControls;
    private MutableLiveData<String> mSelectedRoom;

    public MainViewModel (Application application) {
        super(application);
        mRepository = new ControlRepository(application);
        mAllControls = mRepository.getAllControls();
        mSelectedRoom = mRepository.getSelectedGroupRoom();
    }

    LiveData<List<Control>> getAllControls() { return mAllControls; }

    LiveData<List<String>> getDistinctGroups() {return  mRepository.getDistinctGroups(); }

    LiveData<List<String>> getDistinctRooms(String group) {return  mRepository.getAllRoomsOfGroup(group); }

    public void updateStatus(String group, String room, String name, float status){
        mRepository.updateControlStatus(group,room,name,status);
    }

    public MutableLiveData<String> getSelectedGroupRoom(){
        return mSelectedRoom;
    }


}
