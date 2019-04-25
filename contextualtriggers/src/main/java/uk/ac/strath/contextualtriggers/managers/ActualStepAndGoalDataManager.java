package uk.ac.strath.contextualtriggers.managers;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutionException;

import uk.ac.strath.contextualtriggers.data.CacheDatabase;
import uk.ac.strath.contextualtriggers.data.DataEntity;
import uk.ac.strath.contextualtriggers.data.DayData;
import uk.ac.strath.contextualtriggers.data.StepAndGoalData;
import uk.ac.strath.contextualtriggers.intentReceivers.StepAndGoalIntentReceiver;

public class ActualStepAndGoalDataManager extends DataManager<StepAndGoalData> {

    StepAndGoalData stepGoalData;
    private StepAndGoalIntentReceiver receiver;
    private final IBinder binder = new LocalBinder();

    CacheDatabase db;

    public class LocalBinder extends Binder {
        public IDataManager<StepAndGoalData> getInstance() {
            return ActualStepAndGoalDataManager.this;
        }
    }

    public ActualStepAndGoalDataManager(){
        Log.d("StepAndGoalIntentReceiver","Starting");
        try {
            setup();
        } catch (ExecutionException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {

            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void configureReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("uk.ac.strath.contextualtriggers.step");
        receiver = new StepAndGoalIntentReceiver();
        registerReceiver(receiver, filter);
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

    private void setup() throws ExecutionException, InterruptedException {
        db = CacheDatabase.getDatabase(this);

        List<DataEntity> l = db.getAllOfType(StepAndGoalData.class.toString()).get();

        if(l.size() == 0){
            stepGoalData = new StepAndGoalData();
        }
        else{
            stepGoalData = (StepAndGoalData) l.get(0).data;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        monitor(intent);
        return START_STICKY;
    }

    private void monitor(Intent intent){
        LocalDate today = LocalDate.now();
        DayData day = new DayData(intent.getIntExtra("steps", 0),intent.getIntExtra("goal", 0),today);
        stepGoalData.updateDay(day);
        Log.d("ActualStepAndGoalDataManager", "Starting");
        sendUpdate(stepGoalData);
    }

}
