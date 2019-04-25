package uk.ac.strath.contextualtriggers.managers;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import uk.ac.strath.contextualtriggers.data.VoidData;

public class NotificationDataManager extends DataManager<VoidData> {

    Integer goal;
    private final IBinder binder = new LocalBinder();

    public class LocalBinder extends Binder {
        public IDataManager<VoidData> getInstance() {
            return NotificationDataManager.this;
        }
    }

    public NotificationDataManager()
    {
        setup();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //Not sure if this is required
        //Needed if onStartCommand not called automatically
        Log.d("NotificationDataManage", "Binding");
        return binder;
    }

    private void setup() {
        goal = 10000;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        monitor();
        return START_STICKY;
    }

    private void monitor(){
        sendUpdate(new VoidData());
    }


}
