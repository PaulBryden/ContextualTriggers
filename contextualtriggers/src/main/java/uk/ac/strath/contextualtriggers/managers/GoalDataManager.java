package uk.ac.strath.contextualtriggers.managers;

import android.support.annotation.RequiresPermission;

import uk.ac.strath.contextualtriggers.Logger;
import uk.ac.strath.contextualtriggers.conditions.DataCondition;
import uk.ac.strath.contextualtriggers.data.GoalData;

public class GoalDataManager implements IDataManager<GoalData>, IDataManagerSource, Runnable
{
    Logger logger;
    boolean isRunning=false;
    GoalData GoalData;
    private DataManager<GoalData> dataManager;
    private static GoalDataManager singletonGoalDataManager= null;


    private GoalDataManager()
    {
        dataManager= new DataManager<GoalData>();
        logger = Logger.getInstance();
        GoalData.steps=10000;
        singletonGoalDataManager=this;
    }

    public static GoalDataManager getInstance()
    {
        if (singletonGoalDataManager == null)
            singletonGoalDataManager = new GoalDataManager();
        return singletonGoalDataManager;
    }

    public void register(DataCondition<GoalData> dataCondition)
    {
        dataManager.register(dataCondition);
    }

    private void sendUpdate(GoalData data)
    {
        dataManager.sendUpdate(data);
    }

    @RequiresPermission("android.permission.ACCESS_FINE_LOCATION")
    public void run()
    {
    }
    @Override
    public void start()
    {
        sendUpdate(GoalData);
    }

}
