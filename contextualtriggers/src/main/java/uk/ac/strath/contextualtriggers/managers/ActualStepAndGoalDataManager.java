package uk.ac.strath.contextualtriggers.managers;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import uk.ac.strath.contextualtriggers.GoalIntentReceiver;
import uk.ac.strath.contextualtriggers.Logger;
import uk.ac.strath.contextualtriggers.StepIntentReceiver;
import uk.ac.strath.contextualtriggers.data.StepAndGoalData;

public class ActualStepAndGoalDataManager extends DataManager<StepAndGoalData> implements IDataManager<StepAndGoalData> {

    Logger logger;
    StepAndGoalData stepGoalData;
    private StepIntentReceiver receiver;
    private GoalIntentReceiver receiver2;
    private final IBinder binder = new LocalBinder();
    public class LocalBinder extends Binder {
        public IDataManager getInstance() {
            return ActualStepAndGoalDataManager.this;
        }
    }

    public ActualStepAndGoalDataManager(){
        Log.d("StepIntentReceiver","Starting");
        setup();
    }

    private void configureReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("uk.ac.strath.contextualtriggers.step");
        receiver = new StepIntentReceiver();
        registerReceiver(receiver, filter);
        IntentFilter filter2 = new IntentFilter();
        filter2.addAction("uk.ac.strath.contextualtriggers.goal");
        receiver2 = new GoalIntentReceiver();
        registerReceiver(receiver2, filter2);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //Not sure if this is required
        //Needed if onStartCommand not called automatically
        Log.d("SimulatedStepDataManage", "Binding");
        configureReceiver();
        return binder;
    }

    private void setup() {
        stepGoalData = new StepAndGoalData();
        logger = Logger.getInstance();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        monitor(intent);
        return START_STICKY;
    }

    private void monitor(Intent intent){

            stepGoalData.steps.steps = intent.getIntExtra("steps", 0);
            stepGoalData.goal.steps = intent.getIntExtra("goal", 0);

        logger.log("Actual Steps: " + stepGoalData.steps.steps + "\n");
        logger.log("Actual Goal: " + stepGoalData.goal.steps + "\n");
        Log.d("ActualStepAndGoalDataManager", "Starting");
        sendUpdate(stepGoalData);
    }

}
