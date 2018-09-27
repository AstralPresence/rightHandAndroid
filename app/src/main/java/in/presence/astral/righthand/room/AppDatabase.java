package in.presence.astral.righthand.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import in.presence.astral.righthand.dao.ControlDao;

@Database(entities = {Control.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ControlDao controlObjectDao();


    private static volatile AppDatabase INSTANCE;

    static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "rh_db")
                            .build();

                }
            }
        }
        return INSTANCE;
    }
}
