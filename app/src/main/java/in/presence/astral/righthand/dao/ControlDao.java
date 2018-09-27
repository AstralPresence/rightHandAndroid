package in.presence.astral.righthand.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import in.presence.astral.righthand.room.Control;

@Dao
public interface ControlDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Control controlObject);

    @Query("DELETE FROM controls")
    void deleteAll();

    @Query("SELECT * from controls")
    LiveData<List<Control>> getAllControls();

}
