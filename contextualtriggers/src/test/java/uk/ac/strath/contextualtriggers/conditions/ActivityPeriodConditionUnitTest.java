package uk.ac.strath.contextualtriggers.conditions;

import com.google.android.gms.location.DetectedActivity;

import org.junit.Before;
import org.junit.Test;

import uk.ac.strath.contextualtriggers.data.ActivityData;

import static com.google.android.gms.location.DetectedActivity.ON_BICYCLE;
import static com.google.android.gms.location.DetectedActivity.STILL;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ActivityPeriodConditionUnitTest {

    private MockDataManager<ActivityData> manager;
    private ActivityPeriodCondition condition;

    @Before
    public void setup() {
        manager = new MockDataManager<>();
        condition = new ActivityPeriodCondition(10, STILL, manager);
    }

    @Test
    public void testNoActivityDataReceivedYet() {
        assertFalse(condition.isSatisfied());
    }

    @Test
    public void testStillActivitySatisfied() {
        manager.sendUpdate(new ActivityData(new DetectedActivity(STILL, 100), System.currentTimeMillis() - 20000));
        assertTrue(condition.isSatisfied());
    }

    @Test
    public void testStillConditionNotSatisfiedForMinimumTime() {
        manager.sendUpdate(new ActivityData(new DetectedActivity(STILL, 100), System.currentTimeMillis() - 5000));
        assertFalse(condition.isSatisfied());
    }

    @Test
    public void testStillConditionNotSatisfiedAsIncorrectActivity() {
        manager.sendUpdate(new ActivityData(new DetectedActivity(STILL, 100), System.currentTimeMillis() - 30000));
        manager.sendUpdate(new ActivityData(new DetectedActivity(ON_BICYCLE, 100), System.currentTimeMillis() - 20000));
        assertFalse(condition.isSatisfied());
    }

}
