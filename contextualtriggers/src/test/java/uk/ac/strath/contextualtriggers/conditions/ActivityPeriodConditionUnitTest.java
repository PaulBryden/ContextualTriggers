package uk.ac.strath.contextualtriggers.conditions;

import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.android.gms.location.DetectedActivity;

import org.junit.Test;

import uk.ac.strath.contextualtriggers.data.ActivityData;
import uk.ac.strath.contextualtriggers.managers.DataManager;

import static com.google.android.gms.location.DetectedActivity.ON_BICYCLE;
import static com.google.android.gms.location.DetectedActivity.STILL;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ActivityPeriodConditionUnitTest {

    private class MockActivityDataManager extends DataManager<ActivityData> {
        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        public void sendMockUpdate(int activity, long timestamp) {
            sendUpdate(new ActivityData(new DetectedActivity(activity, 100), timestamp));
        }
    }

    /**
     * Tests what happens when no activity has been sent to the condition yet.
     */
    @Test
    public void testNoActivityDataReceivedYet() {
        MockActivityDataManager manager = new MockActivityDataManager();
        ActivityPeriodCondition condition = new ActivityPeriodCondition(10000, STILL, manager);
        assertFalse(condition.isSatisfied());
    }

    /**
     * Tests what happens when the user has been still for 10 seconds.
     */
    @Test
    public void testStillActivitySatisfied() {
        MockActivityDataManager manager = new MockActivityDataManager();
        ActivityPeriodCondition condition = new ActivityPeriodCondition(10000, STILL, manager);
        manager.sendMockUpdate(STILL, System.currentTimeMillis() - 20000);
        assertTrue(condition.isSatisfied());
    }

    /**
     * Checks what happens when the user has not been still for 10 seconds because they have only
     * been still for 5 seconds.
     */
    @Test
    public void testStillConditionNotSatisfiedForMinimumTime() {
        MockActivityDataManager manager = new MockActivityDataManager();
        ActivityPeriodCondition condition = new ActivityPeriodCondition(10000, STILL, manager);
        manager.sendMockUpdate(STILL, System.currentTimeMillis() - 5000);
        assertFalse(condition.isSatisfied());
    }

    /**
     * Tests what happens when the user is not still.
     */
    @Test
    public void testStillConditionNotSatisfiedAsIncorrectActivity() {
        MockActivityDataManager manager = new MockActivityDataManager();
        ActivityPeriodCondition condition = new ActivityPeriodCondition(10000, STILL, manager);
        manager.sendMockUpdate(STILL, System.currentTimeMillis() - 30000);
        manager.sendMockUpdate(ON_BICYCLE, System.currentTimeMillis() - 20000);
        assertFalse(condition.isSatisfied());
    }

}
