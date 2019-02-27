package in.presence.astral.righthand.room;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import in.presence.astral.righthand.dao.ControlDao;

public class ControlRepository {

    private ControlDao mControlDao;


    public ControlRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mControlDao = db.controlObjectDao();
    }

    public LiveData<List<Control>> getAllControls() {
        return mControlDao.getAllControls();
    }


    public LiveData<List<Control>> getRoomControls(String group, String room) {
        return mControlDao.getRoomControls(group,room);
    }

    public LiveData<List<Control>> getRoomLocks(String group, String room) {
        return mControlDao.getRoomLocks(group,room);
    }

    public LiveData<List<Control>> getRoomCameras(String group, String room) {
        return mControlDao.getRoomCameras(group,room);
    }

    public LiveData<List<String>> getDistinctGroups() {
        return mControlDao.getAllGroups();
    }

    public LiveData<List<String>> getAllRoomsOfGroup(String group) {
        return mControlDao.getAllRoomsOfGroup(group);
    }

    public void insertControls (Control controls) {
        new InsertAsyncTask(mControlDao).execute(controls);
    }


    public void updateControlStatus(String group, String room, String name,float status){
        Control control = new Control(group,room,name,null,null,status,null);
        new UpdateAsyncTask(mControlDao).execute(control);

    }


    private static class InsertAsyncTask extends AsyncTask<Control, Void, Void> {

        private ControlDao mAsyncTaskDao;

        InsertAsyncTask(ControlDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Control... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }


    private static class UpdateAsyncTask extends AsyncTask<Control, Void, Void> {

        private ControlDao mAsyncTaskDao;

        UpdateAsyncTask(ControlDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Control... params) {
            Control control = params[0];
            mAsyncTaskDao.update(control.getStatus(),control.getGroup(),control.getRoom(),control.getName());
            return null;
        }
    }
}
