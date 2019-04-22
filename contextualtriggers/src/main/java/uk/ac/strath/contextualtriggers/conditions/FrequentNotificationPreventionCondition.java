package uk.ac.strath.contextualtriggers.conditions;

import uk.ac.strath.contextualtriggers.data.VoidData;
import uk.ac.strath.contextualtriggers.managers.IDataManager;

/**
 * Basic condition that checks if a notification has been sent recently.
 * Condition is satisfied if time elapsed since last condition is more than
 * specified amount.
 */
public class FrequentNotificationPreventionCondition extends DataCondition<VoidData>
{

    private long lastNotificationSent;
    private int minimumTimeElapsed; // in seconds

    public FrequentNotificationPreventionCondition(int minimumTimeElapsed, IDataManager<VoidData> dataManager)
    {
        super(dataManager);
        this.minimumTimeElapsed = minimumTimeElapsed; /*seconds*/
        lastNotificationSent = -1;
    }

    @Override
    public void notifyUpdate(VoidData data)
    {
        // Override since an update always means condition isn't satisfied,
        // so no need to notify the Trigger of the change.
        lastNotificationSent = System.currentTimeMillis();
        super.notifyUpdate(data);
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
