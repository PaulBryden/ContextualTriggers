package uk.ac.strath.contextualtriggers.managers;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;

import uk.ac.strath.contextualtriggers.Logger;
import uk.ac.strath.contextualtriggers.MainApplication;
import uk.ac.strath.contextualtriggers.conditions.DataCondition;
import uk.ac.strath.contextualtriggers.data.StepData;

public class StepDataManager extends DataManager<StepData> implements IDataManager<StepData>
{
    Logger logger;
   // boolean isRunning=false;
    StepData stepData;
   // private DataManager<StepData> dataManager;
//    private static StepDataManager singletonStepDataManager= null;
    private final IBinder binder = new LocalBinder();


    public class LocalBinder extends Binder {
        public IDataManager getInstance(){
                    return StepDataManager.this;
        }
    }

//    public static StepDataManager getInstance()
//    {
//        if (singletonStepDataManager == null)
//            singletonStepDataManager = new StepDataManager();
//        return singletonStepDataManager;
//    }

//    private StepDataManager()
//    {
////        stepData = new StepData();
////     //   dataManager= new DataManager<StepData>();
////        logger = Logger.getInstance();
////       // singletonStepDataManager=this;
////    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //Not sure if this is required
        //Needed if onStartCommand not called automatically
        setup();
        return binder;
    }

//    public void register(DataCondition<StepData> dataCondition)
//    {
//        dataManager.register(dataCondition);
//    }

//    private void sendUpdate(StepData data)
//    {
//        dataManager.sendUpdate(data);
//    }

    private void setup(){
        stepData = new StepData();
        //   dataManager= new DataManager<StepData>();
        logger = Logger.getInstance();
        // singletonStepDataManager=this;
    }

//    @Override
//    public void start() {
//        /*//Currently starts but could bind service
//        Intent service = new Intent(MainApplication.getAppContext(), StepDataManagerService.class);
//        MainApplication.getAppContext().startService(service);*/
//    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        setup();
        stepData.steps += 100;
        logger.log("Steps: " + stepData.steps + "\n");
        sendUpdate(stepData);
        try
        {
            Thread.sleep(10000);
        } catch (Exception e)
        {
        }
        return START_STICKY;
    }
}
