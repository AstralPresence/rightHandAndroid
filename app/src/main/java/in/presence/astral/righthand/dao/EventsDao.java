package in.presence.astral.righthand.dao;


import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import in.presence.astral.righthand.room.Event;
import in.presence.astral.righthand.room.User;

@Dao
public interface EventsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Event event);

    @Query("DELETE FROM events")
    void deleteAll();

    @Query("SELECT * from events where topic = :topic")
    LiveData<List<Event>> getControlEvents(String topic);

}
