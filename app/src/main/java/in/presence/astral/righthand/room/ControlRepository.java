package in.presence.astral.righthand.room;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import in.presence.astral.righthand.dao.ControlDao;

public class ControlRepository {

    private ControlDao mControlDao;
    private LiveData<List<Control>> mAllControls;

    public ControlRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mControlDao = db.controlObjectDao();
        mAllControls = mControlDao.getAllControls();
    }

    public LiveData<List<Control>> getAllWords() {
        return mAllControls;
    }

    public void insert (Control control) {
        new insertAsyncTask(mControlDao).execute(control);
    }

    private static class insertAsyncTask extends AsyncTask<Control, Void, Void> {

        private ControlDao mAsyncTaskDao;

        insertAsyncTask(ControlDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Control... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
