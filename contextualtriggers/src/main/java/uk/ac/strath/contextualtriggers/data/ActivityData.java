package uk.ac.strath.contextualtriggers.data;

import com.google.android.gms.location.DetectedActivity;

public class ActivityData extends AbstractData {

    private DetectedActivity activity;

    public ActivityData(DetectedActivity activity) {
        this.activity = activity;
    }

    public int getActivityType() {
        return activity.getType();
    }
}
