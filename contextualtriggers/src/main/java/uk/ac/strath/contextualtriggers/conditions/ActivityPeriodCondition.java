package uk.ac.strath.contextualtriggers.conditions;

import com.google.android.gms.location.DetectedActivity;

import uk.ac.strath.contextualtriggers.data.ActivityData;
import uk.ac.strath.contextualtriggers.managers.IDataManager;

/**
 * A condition that checks whether the user has been doing the target activity for longer than the
 * target time.
 */
public class ActivityPeriodCondition extends DataCondition<ActivityData> {

    private long activityStarted;
    private int activityTypeUnderExamination;
    private int minimumTimeElapsed; // in seconds

    public ActivityPeriodCondition(int minimumTimeElapsed, int activityType, IDataManager<ActivityData> dataManager) {
        super(dataManager, 30, new ActivityData(new DetectedActivity(0, 0)));
        this.minimumTimeElapsed = minimumTimeElapsed; /*seconds*/
        activityStarted = System.currentTimeMillis();
        activityTypeUnderExamination = activityType;
    }

    @Override
    public void notifyUpdate(ActivityData data) {
        // Override since an update always means condition isn't satisfied,
        // so no need to notify the Trigger of the change.
        if (data.getActivityType() != getData().getActivityType()) {
            activityStarted = data.getTimestamp();
        }
        super.notifyUpdate(data);
    }

    @Override
    public boolean isSatisfied() {
        return getData().getActivityType() == activityTypeUnderExamination &&
                (System.currentTimeMillis() - activityStarted) > minimumTimeElapsed;
    }

}
