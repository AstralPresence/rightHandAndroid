package in.presence.astral.righthand.dao;


import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import in.presence.astral.righthand.room.Mode;
import in.presence.astral.righthand.room.Control;

@Dao
public interface ModesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Mode mode);

    @Query("DELETE FROM modes")
    void deleteAll();

    @Query("SELECT DISTINCT name from modes")
    LiveData<List<String>> getAllModes();

    @Query("SELECT * from modes where name = :name")
    LiveData<List<Mode>> getModeParticulars(String name);

}
