package uk.ac.strath.contextualtriggers.managers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import uk.ac.strath.contextualtriggers.Logger;
import uk.ac.strath.contextualtriggers.data.StepData;

public class SimulatedGoalDataManager extends DataManager<Integer> implements IDataManager<Integer> {

    Logger logger;
    Integer goal;
    private final IBinder binder = new LocalBinder();

    public class LocalBinder extends Binder {
        public IDataManager getInstance() {
            return SimulatedGoalDataManager.this;
        }
    }

    public SimulatedGoalDataManager()
    {
        setup();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //Not sure if this is required
        //Needed if onStartCommand not called automatically
        Log.d("SimulatedStepDataManage", "Binding");
        return binder;
    }

    private void setup() {
        goal = 10000;
        logger = Logger.getInstance();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        monitor();
        alarm();
        return START_STICKY;
    }

    private void monitor(){
        sendUpdate(goal);
    }

    private void alarm(){
        AlarmManager alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        Intent sgd = new Intent(this, SimulatedGoalDataManager.class);
        PendingIntent alarmIntent = PendingIntent.getService(this, 0, sgd, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 5000, alarmIntent);
    }

}
