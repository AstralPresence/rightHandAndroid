package in.presence.astral.righthand.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import in.presence.astral.righthand.dao.AccessGroupsDao;
import in.presence.astral.righthand.dao.ControlDao;
import in.presence.astral.righthand.dao.EventsDao;
import in.presence.astral.righthand.dao.ModesDao;
import in.presence.astral.righthand.dao.UsersDao;

@Database(entities = {Control.class, Mode.class, User.class,AccessGroup.class,Event.class}, version = 4)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ControlDao controlObjectDao();
    public abstract ModesDao modesDao();
    public abstract UsersDao usersDao();
    public abstract AccessGroupsDao accessGroupsDao();
    public abstract EventsDao eventsDao();


    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "rh_db").fallbackToDestructiveMigration()
                            .build();

                }
            }
        }
        return INSTANCE;
    }
}
