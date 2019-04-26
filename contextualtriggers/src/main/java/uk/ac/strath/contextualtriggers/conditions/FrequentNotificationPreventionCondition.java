package uk.ac.strath.contextualtriggers.conditions;

import uk.ac.strath.contextualtriggers.data.VoidData;
import uk.ac.strath.contextualtriggers.managers.IDataManager;

/**
 * Basic condition that checks if a notification has been sent recently.
 * Condition is satisfied if time elapsed since last condition is more than
 * specified amount.
 */
public class FrequentNotificationPreventionCondition extends DataCondition<VoidData> {

    private int minimumTimeElapsed; // in seconds

    public FrequentNotificationPreventionCondition(int minimumTimeElapsed, IDataManager<VoidData> dataManager) {
        super(dataManager);
        this.minimumTimeElapsed = minimumTimeElapsed; /*seconds*/
    }

    @Override
    public void notifyUpdate(VoidData data) {
        // Override since an update always means condition isn't satisfied,
        // so no need to notify the Trigger of the change.
        super.notifyUpdate(data);
    }

    @Override
    public boolean hasStaleData() {
        return false;
    }

    public boolean isSatisfied() {
        if (getData() == null) {
            return true;
        }
        return System.currentTimeMillis() - getData().getTimestamp() > minimumTimeElapsed * 1000;
    }
}
