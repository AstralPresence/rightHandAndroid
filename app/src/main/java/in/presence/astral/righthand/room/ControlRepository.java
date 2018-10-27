package in.presence.astral.righthand.room;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import in.presence.astral.righthand.dao.ControlDao;

public class ControlRepository {

    private ControlDao mControlDao;

    public final MutableLiveData<String> selectedGroupRoom = new MutableLiveData<String>();

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

    public LiveData<List<String>> getDistinctGroups() {
        return mControlDao.getAllGroups();
    }

    public LiveData<List<String>> getAllRoomsOfGroup(String group) {
        return mControlDao.getAllRoomsOfGroup(group);
    }

    public void insertControls (Control controls) {
        new InsertAsyncTask(mControlDao).execute(controls);
    }

    public MutableLiveData< String > getSelectedGroupRoom( )
    {
        return selectedGroupRoom;
    }

    public void setSelectedGroupRoom( String groupRoom )
    {
        selectedGroupRoom.postValue( groupRoom );
    }

    public void updateControlStatus(String group, String room, String name,float status){
        mControlDao.update(status,group,room,name);

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
}
