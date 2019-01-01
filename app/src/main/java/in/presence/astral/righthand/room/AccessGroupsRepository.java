package in.presence.astral.righthand.room;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;
import in.presence.astral.righthand.dao.AccessGroupsDao;
import in.presence.astral.righthand.dao.UsersDao;

public class AccessGroupsRepository {

    private AccessGroupsDao mAccessGroupsDao;


    public AccessGroupsRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mAccessGroupsDao = db.accessGroupsDao();
    }

    public LiveData<List<AccessGroup>> getAllAccessGroups() {
        return mAccessGroupsDao.getAllAccessGroups();
    }



    public LiveData<List<String>> getAllAccessGroupsNames() {
        return mAccessGroupsDao.getAllAGNames();
    }




    public void insertAccessGroup(AccessGroup accessGroup) {
        new InsertAsyncTask(mAccessGroupsDao).execute(accessGroup);
    }




    private static class InsertAsyncTask extends AsyncTask<AccessGroup, Void, Void> {

        private AccessGroupsDao mAsyncTaskDao;

        InsertAsyncTask(AccessGroupsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final AccessGroup... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }


}
