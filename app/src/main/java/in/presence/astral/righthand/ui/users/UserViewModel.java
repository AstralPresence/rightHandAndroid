package in.presence.astral.righthand.ui.users;

import android.app.Application;

import org.w3c.dom.ls.LSException;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import in.presence.astral.righthand.room.AccessGroup;
import in.presence.astral.righthand.room.AccessGroupsRepository;
import in.presence.astral.righthand.room.Control;
import in.presence.astral.righthand.room.ControlRepository;
import in.presence.astral.righthand.room.User;
import in.presence.astral.righthand.room.UsersRepository;

public class UserViewModel extends AndroidViewModel {

    private UsersRepository mRepository;

    private ControlRepository controlsRepository;
    private AccessGroupsRepository agRepository;


    public UserViewModel (Application application) {
        super(application);
        mRepository = new UsersRepository(application);
        agRepository = new AccessGroupsRepository(application);
        controlsRepository = new ControlRepository(application);

    }



    LiveData<List<String>> getDistinctGroups() {return  controlsRepository.getDistinctGroups(); }

    LiveData<List<String>> getDistinctRooms(String group) {return  controlsRepository.getAllRoomsOfGroup(group); }

    LiveData<List<User>> getUsers() {
        return mRepository.getAllUsers();
    }

    LiveData<List<String>> getAccessGroupNames() {
        return agRepository.getAllAccessGroupsNames();
    }


    public void insert(User user) { mRepository.insertUser(user); }
}
