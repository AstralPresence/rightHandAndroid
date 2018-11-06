package in.presence.astral.righthand.room;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;
import in.presence.astral.righthand.dao.EventsDao;
import in.presence.astral.righthand.dao.ModesDao;

public class EventsRepository {

    private EventsDao eventsDao;


    public EventsRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        eventsDao = db.eventsDao();
    }

    public LiveData<List<Event>> getEventsOfTopic(String topic) {
        return eventsDao.getControlEvents(topic);
    }

    public void insertEvent (Event event) {
        new InsertAsyncTask(eventsDao).execute(event);
    }


    private static class InsertAsyncTask extends AsyncTask<Event, Void, Void> {

        private EventsDao mAsyncTaskDao;

        InsertAsyncTask(EventsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Event... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }


}
