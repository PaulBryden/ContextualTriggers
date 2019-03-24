package uk.ac.strath.contextualtriggers.managers;

import android.support.annotation.RequiresPermission;

import uk.ac.strath.contextualtriggers.Logger;
import uk.ac.strath.contextualtriggers.conditions.DataCondition;
import uk.ac.strath.contextualtriggers.data.StepData;

public class StepDataManager implements IDataManager<StepData>, IDataManagerSource, Runnable
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

    public void run()
    {
        while(isRunning)
        {
            stepData.steps += 100;
            logger.log("Steps: " + stepData.steps + "\n");
            sendUpdate(stepData);

            try
            {
                Thread.sleep(10000);
            } catch (Exception e)
            {

            }
        }
    }

    @Override
    public void start()
    {
        if(!isRunning)
        {
            Thread aThread = new Thread(this);
            isRunning = true;
            aThread.start();
        }
    }

}
