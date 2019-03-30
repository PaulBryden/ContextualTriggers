package uk.ac.strath.contextualtriggers.managers;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;

import uk.ac.strath.contextualtriggers.Logger;
import uk.ac.strath.contextualtriggers.MainApplication;
import uk.ac.strath.contextualtriggers.conditions.DataCondition;
import uk.ac.strath.contextualtriggers.data.StepData;

public class StepDataManager implements IDataManager<StepData>, IDataManagerSource
{
    Logger logger;
    boolean isRunning=false;
    StepData stepData;
    private DataManager<StepData> dataManager;
    private static StepDataManager singletonStepDataManager= null;


    public static StepDataManager getInstance()
    {
        if (singletonStepDataManager == null)
            singletonStepDataManager = new StepDataManager();
        return singletonStepDataManager;
    }

    private StepDataManager()
    {
        stepData = new StepData();
        dataManager= new DataManager<StepData>();
        logger = Logger.getInstance();
        singletonStepDataManager=this;
    }

    public void register(DataCondition<StepData> dataCondition)
    {
        dataManager.register(dataCondition);
    }

    private void sendUpdate(StepData data)
    {
        dataManager.sendUpdate(data);
    }

    @Override
    public void start() {
        //Currently starts but could bind service
        Intent service = new Intent(MainApplication.getAppContext(), StepDataManagerService.class);
        MainApplication.getAppContext().startService(service);
    }

    public class StepDataManagerService extends Service{

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {

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

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }
}
