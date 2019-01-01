package in.presence.astral.righthand.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;


/**
 * Created by DevOpsTrends on 7/2/2017.
 */

public class DbSyncService  extends IntentService {

    public DbSyncService(String name) {
        super(name);
    }

    public DbSyncService() {
        super("DbSyncService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        DBSyncTask.syncData(getApplicationContext());
    }
}
