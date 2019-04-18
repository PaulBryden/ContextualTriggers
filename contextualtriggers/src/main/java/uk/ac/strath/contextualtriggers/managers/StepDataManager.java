package uk.ac.strath.contextualtriggers.managers;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import uk.ac.strath.contextualtriggers.Logger;
import uk.ac.strath.contextualtriggers.data.StepData;

public class StepDataManager extends DataManager<StepData> implements IDataManager<StepData> {

    Logger logger;
    StepData stepData;
    private final IBinder binder = new LocalBinder();

    public class LocalBinder extends Binder {
        public IDataManager getInstance() {
            return StepDataManager.this;
        }
    }
    StepDataManager()
    {
        setup();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //Not sure if this is required
        //Needed if onStartCommand not called automatically
        Log.d("StepDataManager", "Binding");
        return binder;
    }

    private void setup() {
        stepData = new StepData();
        logger = Logger.getInstance();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStart(intent, startId);
        stepData.steps += 100;
        logger.log("Steps: " + stepData.steps + "\n");
        Log.d("StepDataManager", "Starting");
        sendUpdate(stepData);
        return START_STICKY;
    }
}
