package uk.ac.strath.contextualtriggers.conditions;

import org.junit.Test;

import uk.ac.strath.contextualtriggers.data.TimeOfDayData;

import static com.google.android.gms.awareness.fence.TimeFence.TIME_INTERVAL_AFTERNOON;
import static com.google.android.gms.awareness.fence.TimeFence.TIME_INTERVAL_EVENING;
import static com.google.android.gms.awareness.fence.TimeFence.TIME_INTERVAL_MORNING;
import static com.google.android.gms.awareness.fence.TimeFence.TIME_INTERVAL_WEEKDAY;
import static com.google.android.gms.awareness.fence.TimeFence.TIME_INTERVAL_WEEKEND;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AcceptableTimeConditionUnitTest {

    @Test
    public void testNoDataReceivedYet() {
        MockDataManager<TimeOfDayData> manager = new MockDataManager<>();
        TimeOfDayData data = new TimeOfDayData(new int[]{TIME_INTERVAL_AFTERNOON});
        AcceptableTimeCondition condition = new AcceptableTimeCondition(data, manager);
        assertFalse(condition.isSatisfied());
    }

    /**
     * Tests what happens when the current time intervals are MORNING and AFTERNOON and the target
     * time interval is MORNING.
     */
    @Test
    public void testTargetInCurrentIntervals() {
        MockDataManager<TimeOfDayData> manager = new MockDataManager<>();
        TimeOfDayData data = new TimeOfDayData(new int[]{TIME_INTERVAL_AFTERNOON});
        AcceptableTimeCondition condition = new AcceptableTimeCondition(data, manager);
        manager.sendUpdate(new TimeOfDayData(new int[]{TIME_INTERVAL_MORNING, TIME_INTERVAL_AFTERNOON}));
        assertTrue(condition.isSatisfied());
    }

    /**
     * Tests what happens when the current time intervals are WEEKDAY and MORNING and the target
     * time intervals are EVENING and WEEKEND.
     */
    @Test
    public void testTargetNotInCurrentIntervals() {
        MockDataManager<TimeOfDayData> manager = new MockDataManager<>();
        TimeOfDayData data = new TimeOfDayData(new int[]{TIME_INTERVAL_EVENING, TIME_INTERVAL_WEEKEND});
        AcceptableTimeCondition condition = new AcceptableTimeCondition(data, manager);
        manager.sendUpdate(new TimeOfDayData(new int[]{TIME_INTERVAL_WEEKDAY, TIME_INTERVAL_MORNING}));
        assertFalse(condition.isSatisfied());
    }

}
