package uk.ac.strath.contextualtriggers.data;

import com.google.android.gms.location.DetectedActivity;

import java.lang.reflect.Type;

public class ActivityData extends AbstractData {

    private DetectedActivity activity;

    public ActivityData(DetectedActivity activity) {
        this.activity = activity;
    }

    public ActivityData(DetectedActivity activity, long timestamp) {
        super(timestamp);
        this.activity = activity;
    }

    public int getActivityType() {
        return activity.getType();
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof ActivityData){
            return ((ActivityData) o).activity.equals(this.activity) && super.equals(o);
        }
        return false;
    }

    public static Type getType() {
        return ActivityData.class;
    }
}
