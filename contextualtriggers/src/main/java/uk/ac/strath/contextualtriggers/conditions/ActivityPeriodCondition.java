package uk.ac.strath.contextualtriggers.conditions;

import com.google.android.gms.location.DetectedActivity;

import uk.ac.strath.contextualtriggers.data.ActivityData;
import uk.ac.strath.contextualtriggers.managers.IDataManager;

/**
 * Basic condition that checks if a notification has been sent recently.
 * Condition is satisfied if time elapsed since last condition is more than
 * specified amount.
 */
public class ActivityPeriodCondition extends DataCondition<ActivityData>
{

    private long activityStarted;
    private int activityTypeUnderExamination;
    private int minimumTimeElapsed; // in seconds

    public ActivityPeriodCondition(int minimumTimeElapsed, int activityType, IDataManager<ActivityData> dataManager)
    {
        super(dataManager);
        this.minimumTimeElapsed = minimumTimeElapsed; /*seconds*/
        activityStarted = System.currentTimeMillis();
        activityTypeUnderExamination=activityType;
        this.notifyUpdate(new ActivityData(new DetectedActivity(0, 0)));
    }

    @Override
    public void notifyUpdate(ActivityData data)
    {
        // Override since an update always means condition isn't satisfied,
        // so no need to notify the Trigger of the change.
        if(data.getActivityType() != getData().getActivityType())
        {
            activityStarted = data.getTimestamp();
        }
        super.notifyUpdate(data);
    }

    @Override
    public boolean isSatisfied()
    {
        return getData().getActivityType() == activityTypeUnderExamination &&
                (System.currentTimeMillis() - activityStarted) > minimumTimeElapsed;
    }
}
