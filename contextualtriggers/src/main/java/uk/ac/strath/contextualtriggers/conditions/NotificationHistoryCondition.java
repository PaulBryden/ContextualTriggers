package uk.ac.strath.contextualtriggers.conditions;

import uk.ac.strath.contextualtriggers.managers.IDataManager;

/**
 * Basic condition that checks if a notification has been sent recently.
 * Condition is satisfied if time elapsed since last condition is more than
 * specified amount.
 */
public class NotificationHistoryCondition extends DataCondition<Void>
{

    private long lastNotificationSent;
    private int minimumTimeElapsed; // in seconds

    public NotificationHistoryCondition(int minimumTimeElapsed, IDataManager<Void> dataManager)
    {
        super(dataManager);
        this.minimumTimeElapsed = minimumTimeElapsed; /*seconds*/
        lastNotificationSent = -1;
    }

    @Override
    public void notifyUpdate(Void data)
    {
        // Override since an update always means condition isn't satisfied,
        // so no need to notify the Trigger of the change.
        lastNotificationSent = System.currentTimeMillis();
    }

    @Override
    public boolean isSatisfied()
    {
        if (lastNotificationSent < 0)
        {
            return true;
        }
        long now = System.currentTimeMillis();
        return now - lastNotificationSent > 1000 * minimumTimeElapsed;
    }
}
