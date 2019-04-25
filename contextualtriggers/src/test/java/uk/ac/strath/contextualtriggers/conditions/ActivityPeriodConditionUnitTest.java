package uk.ac.strath.contextualtriggers.conditions;

import com.google.android.gms.location.DetectedActivity;

import org.junit.Test;

import uk.ac.strath.contextualtriggers.data.ActivityData;

import static com.google.android.gms.location.DetectedActivity.ON_BICYCLE;
import static com.google.android.gms.location.DetectedActivity.STILL;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ActivityPeriodConditionUnitTest {

    /**
     * Tests what happens when no activity has been sent to the condition yet.
     */
    @Test
    public void testNoActivityDataReceivedYet() {
        MockDataManager<ActivityData> manager = new MockDataManager<>();
        ActivityPeriodCondition condition = new ActivityPeriodCondition(10000, STILL, manager);
        assertFalse(condition.isSatisfied());
    }

    /**
     * Tests what happens when the user has been still for 10 seconds.
     */
    @Test
    public void testStillActivitySatisfied() {
        MockDataManager<ActivityData> manager = new MockDataManager<>();
        ActivityPeriodCondition condition = new ActivityPeriodCondition(10000, STILL, manager);
        manager.sendUpdate(new ActivityData(new DetectedActivity(STILL, 100), System.currentTimeMillis() - 20000));
        assertTrue(condition.isSatisfied());
    }

    /**
     * Checks what happens when the user has not been still for 10 seconds because they have only
     * been still for 5 seconds.
     */
    @Test
    public void testStillConditionNotSatisfiedForMinimumTime() {
        MockDataManager<ActivityData> manager = new MockDataManager<>();
        ActivityPeriodCondition condition = new ActivityPeriodCondition(10000, STILL, manager);
        manager.sendUpdate(new ActivityData(new DetectedActivity(STILL, 100), System.currentTimeMillis() - 5000));
        assertFalse(condition.isSatisfied());
    }

    /**
     * Tests what happens when the user is not still.
     */
    @Test
    public void testStillConditionNotSatisfiedAsIncorrectActivity() {
        MockDataManager<ActivityData> manager = new MockDataManager<>();
        ActivityPeriodCondition condition = new ActivityPeriodCondition(10000, STILL, manager);
        manager.sendUpdate(new ActivityData(new DetectedActivity(STILL, 100), System.currentTimeMillis() - 30000));
        manager.sendUpdate(new ActivityData(new DetectedActivity(ON_BICYCLE, 100), System.currentTimeMillis() - 20000));
        assertFalse(condition.isSatisfied());
    }

}
