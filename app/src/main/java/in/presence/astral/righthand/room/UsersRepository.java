package in.presence.astral.righthand.room;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;
import in.presence.astral.righthand.dao.ControlDao;
import in.presence.astral.righthand.dao.UsersDao;

public class UsersRepository {

    private UsersDao mUsersDao;


    public UsersRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mUsersDao = db.usersDao();
    }

    public LiveData<List<User>> getAllUsers() {
        return mUsersDao.getAllUsers();
    }



    public void insertUser(User user) {
        new InsertAsyncTask(mUsersDao).execute(user);
    }




    private static class InsertAsyncTask extends AsyncTask<User, Void, Void> {

        private UsersDao mAsyncTaskDao;

        InsertAsyncTask(UsersDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final User... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }


}
