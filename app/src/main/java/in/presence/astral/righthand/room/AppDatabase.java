package in.presence.astral.righthand.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import in.presence.astral.righthand.dao.ControlDao;

@Database(entities = {Control.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ControlDao controlObjectDao();


    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
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
