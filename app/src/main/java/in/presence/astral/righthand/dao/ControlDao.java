package in.presence.astral.righthand.dao;


import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import in.presence.astral.righthand.room.Control;

@Dao
public interface ControlDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Control controlObject);

    @Query("DELETE FROM controls")
    void deleteAll();

    @Query("SELECT * from controls")
    LiveData<List<Control>> getAllControls();


    @Query("SELECT DISTINCT ctrl_group from controls")
    LiveData<List<String>> getAllGroups();

    @Query("SELECT DISTINCT room from controls where ctrl_group = :group")
    LiveData<List<String>> getAllRoomsOfGroup(String group);


    @Query("UPDATE controls SET status = :status WHERE ctrl_group =:group AND room = :room AND name = :name")
    void update(float status, String group, String room, String name);


    @Query("SELECT * from controls where ctrl_group = :group AND room = :room")
    LiveData<List<Control>> getRoomControls(String group, String room);

}
