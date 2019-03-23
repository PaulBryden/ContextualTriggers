package uk.ac.strath.contextualtriggers.managers;

import uk.ac.strath.contextualtriggers.conditions.DataCondition;
import uk.ac.strath.contextualtriggers.data.NotificationData;
import uk.ac.strath.contextualtriggers.data.WeatherData;

public class NotificationDataManager implements IDataManager<NotificationData>, IDataManagerSource
{
    private DataManager<NotificationData> dataManager;

    public NotificationDataManager()
    {
        dataManager= new DataManager<NotificationData>();
    }

    public void register(DataCondition<NotificationData> dataCondition)
    {
        dataManager.register(dataCondition);
    }

    private void sendUpdate(NotificationData data)
    {
        dataManager.sendUpdate(data);
    }

    @Override
    public void start()
    {

    }
}
