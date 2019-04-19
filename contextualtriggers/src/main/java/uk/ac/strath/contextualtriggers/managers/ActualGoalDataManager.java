package uk.ac.strath.contextualtriggers.managers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import uk.ac.strath.contextualtriggers.Logger;
import uk.ac.strath.contextualtriggers.MainApplication;
import uk.ac.strath.contextualtriggers.StepIntentReceiver;
import uk.ac.strath.contextualtriggers.data.StepData;

public class ActualGoalDataManager extends DataManager<Integer> implements IDataManager<Integer> {

    Logger logger;
    Integer goalData;
    private final IBinder binder = new LocalBinder();
    public class LocalBinder extends Binder {
        public IDataManager getInstance() {
            return ActualGoalDataManager.this;
        }
    }
    ActualGoalDataManager(){
        Log.d("GoalIntentReceiver","Starting");

        setup();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //Not sure if this is required
        //Needed if onStartCommand not called automatically
        Log.d("ActualGoalDataManager", "Binding");
        return binder;
    }

    private void setup() {
        logger = Logger.getInstance();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        monitor(intent);
        alarm();
        stopSelf();
        return START_STICKY;
    }

    private void monitor(Intent intent){
        try
        {
            goalData = intent.getIntExtra("goal", 0);
        }catch(Exception e)
        {

        }
        logger.log("goal: " + goalData + "\n");
        Log.d("ActualGoalDataManager", "Starting");
        sendUpdate(goalData);
    }

    private void alarm(){
        AlarmManager alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        Intent agd = new Intent(this, ActualGoalDataManager.class);
        PendingIntent alarmIntent = PendingIntent.getService(this, 0, agd, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 5000, alarmIntent);
    }
}
