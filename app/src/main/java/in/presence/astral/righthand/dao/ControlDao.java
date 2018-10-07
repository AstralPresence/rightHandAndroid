package in.presence.astral.righthand.dao;


import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import in.presence.astral.righthand.room.Control;

@Dao
public interface ControlDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Control> controlObject);

    @Query("DELETE FROM controls")
    void deleteAll();

    @Query("SELECT * from controls")
    List<Control> getAllControls();

    @Query("SELECT * from controls where ctrl_group = :group AND room = :room")
    List<Control> getRoomControls(String group, String room);

}
