package uk.ac.strath.contextualtriggers.managers;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import uk.ac.strath.contextualtriggers.data.StepData;

public class SimulatedStepDataManager extends AlarmDataManager<StepData> {

    StepData stepData;
    private final IBinder binder = new LocalBinder();

    public class LocalBinder extends Binder {
        public IDataManager<StepData> getInstance() {
            return SimulatedStepDataManager.this;
        }
    }

    public SimulatedStepDataManager()
    {
        super(5, 60);
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
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        monitor();
        alarm();
        return START_STICKY;
    }

    private void monitor(){
        stepData.steps += 100;
        Log.d("SimulatedStepDataManage", "Starting");
        sendUpdate(stepData);
    }
}
