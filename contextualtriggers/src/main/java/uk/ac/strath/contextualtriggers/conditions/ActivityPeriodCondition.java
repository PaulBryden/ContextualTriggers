package uk.ac.strath.contextualtriggers.conditions;

import com.google.android.gms.location.DetectedActivity;

import uk.ac.strath.contextualtriggers.managers.IDataManager;

/**
 * Basic condition that checks if a notification has been sent recently.
 * Condition is satisfied if time elapsed since last condition is more than
 * specified amount.
 */
public class ActivityPeriodCondition extends DataCondition<DetectedActivity>
{

    private long activityStarted;
    private int activityTypeUnderExamination;
    private int minimumTimeElapsed; // in seconds
    private DetectedActivity currentActivity;

    public ActivityPeriodCondition(int minimumTimeElapsed, int activityType, IDataManager dataManager)
    {
        super(dataManager);
        this.minimumTimeElapsed = minimumTimeElapsed; /*seconds*/
        activityStarted = System.currentTimeMillis();
        currentActivity=getData();
        activityTypeUnderExamination=activityType;
    }

    @Override
    public void notifyUpdate(DetectedActivity data)
    {
        // Override since an update always means condition isn't satisfied,
        // so no need to notify the Trigger of the change.
        if(currentActivity.getType()!=data.getType())
        {
            currentActivity=data;
            activityStarted=System.currentTimeMillis();
        }
        super.notifyUpdate(data);
    }

    @Override
    public boolean isSatisfied()
    {

        if(getData().getType()==currentActivity.getType() && currentActivity.getType()==activityTypeUnderExamination && System.currentTimeMillis()-activityStarted>minimumTimeElapsed)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
