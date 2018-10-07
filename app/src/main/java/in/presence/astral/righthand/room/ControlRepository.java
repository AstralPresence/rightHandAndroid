package in.presence.astral.righthand.room;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;
import in.presence.astral.righthand.dao.ControlDao;

public class ControlRepository {

    private ControlDao mControlDao;
    private LiveData<List<Control>> mAllControls;

    public ControlRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mControlDao = db.controlObjectDao();
    }

    public List<Control> getAllControls() {
        return mControlDao.getAllControls();
    }


    public List<Control> getRoomControls(String group, String room) {
        return mControlDao.getRoomControls(group,room);
    }

    public void insertControls (List<Control> controls) {
        new InsertAsyncTask(mControlDao).execute(controls);
    }

    private static class InsertAsyncTask extends AsyncTask<List<Control>, Void, Void> {

        private ControlDao mAsyncTaskDao;

        InsertAsyncTask(ControlDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final List<Control>... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
