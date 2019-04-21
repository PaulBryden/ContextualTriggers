package uk.ac.strath.contextualtriggers.managers;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import uk.ac.strath.contextualtriggers.Logger;
import uk.ac.strath.contextualtriggers.data.DayData;
import uk.ac.strath.contextualtriggers.intentReceivers.StepAndGoalIntentReceiver;
import uk.ac.strath.contextualtriggers.data.StepAndGoalData;

public class ActualStepAndGoalDataManager extends DataManager<StepAndGoalData> implements IDataManager<StepAndGoalData> {

    Logger logger;
    StepAndGoalData stepGoalData;
    private StepAndGoalIntentReceiver receiver;
    private final IBinder binder = new LocalBinder();
    public class LocalBinder extends Binder {
        public IDataManager getInstance() {
            return ActualStepAndGoalDataManager.this;
        }
    }

    public ActualStepAndGoalDataManager(){
        Log.d("StepAndGoalIntentReceiver","Starting");
        setup();
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
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal0 = Calendar.getInstance();
        Date today = cal0.getTime();
        try
        {
            today = formatter.parse(formatter.format(today));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        DayData day = new DayData(intent.getIntExtra("steps", 0),intent.getIntExtra("goal", 0),today);
        stepGoalData.updateDay(day);
        logger.log("Actual Steps: " + stepGoalData.getDay(today).steps + "\n");
        logger.log("Actual Goal: " + stepGoalData.getDay(today).goal + "\n");
        Log.d("ActualStepAndGoalDataManager", "Starting");
        sendUpdate(stepGoalData);
    }

}
