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

public class SimulatedStepDataManager extends DataManager<StepData> implements IDataManager<StepData> {

    Logger logger;
    StepData stepData;
    private final IBinder binder = new LocalBinder();

    public class LocalBinder extends Binder {
        public IDataManager<StepData> getInstance() {
            return SimulatedStepDataManager.this;
        }
    }

    public SimulatedStepDataManager()
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
        stepData = new StepData();
        logger = Logger.getInstance();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        monitor();
        alarm();
        return START_STICKY;
    }

    private void alarm(){
        AlarmManager alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        Intent isd = new Intent(this, SimulatedStepDataManager.class);
        PendingIntent alarmIntent = PendingIntent.getService(this, 0, isd, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 5000, alarmIntent);
    }

    private void monitor(){
        stepData.steps += 100;
        logger.log("Steps: " + stepData.steps + "\n");
        Log.d("SimulatedStepDataManage", "Starting");
        sendUpdate(stepData);
    }
}
