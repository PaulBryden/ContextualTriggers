package uk.ac.strath.contextualtriggers.managers;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
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
    SimulatedGoalDataManager()
    {
        setup();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //Not sure if this is required
        //Needed if onStartCommand not called automatically
        Log.d("SimulatedStepDataManager", "Binding");
        return binder;
    }

    private void setup() {
        goal = 10000;
        logger = Logger.getInstance();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStart(intent, startId);
        sendUpdate(goal);
        return START_STICKY;
    }
}
