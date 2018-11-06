package in.presence.astral.righthand.room;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;
import in.presence.astral.righthand.dao.ControlDao;
import in.presence.astral.righthand.dao.ModesDao;
import in.presence.astral.righthand.room.Mode;

public class ModeRepository {

    private ModesDao modesDao;


    public ModeRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        modesDao = db.modesDao();
    }

    public LiveData<List<String>> getAllModes() {
        return modesDao.getAllModes();
    }


    public LiveData<List<Mode>> getModeParticulars(String name) {
        return modesDao.getModeParticulars(name);
    }

    public void insertMode (Mode mode) {
        new InsertAsyncTask(modesDao).execute(mode);
    }


    private static class InsertAsyncTask extends AsyncTask<Mode, Void, Void> {

        private ModesDao mAsyncTaskDao;

        InsertAsyncTask(ModesDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Mode... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }


}
