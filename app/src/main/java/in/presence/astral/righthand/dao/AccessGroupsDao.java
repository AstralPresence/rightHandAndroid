package in.presence.astral.righthand.dao;


import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import in.presence.astral.righthand.room.AccessGroup;

@Dao
public interface AccessGroupsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AccessGroup accessGroup);

    @Query("DELETE FROM access_group")
    void deleteAll();

    @Query("SELECT * from access_group")
    LiveData<List<AccessGroup>> getAllAccessGroups();

}
