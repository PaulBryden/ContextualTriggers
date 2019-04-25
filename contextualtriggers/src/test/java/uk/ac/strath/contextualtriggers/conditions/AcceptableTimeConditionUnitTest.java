package uk.ac.strath.contextualtriggers.conditions;

import org.junit.Before;
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

    private MockDataManager<TimeOfDayData> manager;
    private AcceptableTimeCondition afternoonCondition;
    private AcceptableTimeCondition weekendCondition;

    @Before
    public void setup() {
        manager = new MockDataManager<>();
        afternoonCondition = new AcceptableTimeCondition(new TimeOfDayData(new int[]{TIME_INTERVAL_AFTERNOON}), manager);
        weekendCondition = new AcceptableTimeCondition(new TimeOfDayData(new int[]{TIME_INTERVAL_EVENING, TIME_INTERVAL_WEEKEND}), manager);
    }

    @Test
    public void testNoDataReceivedYet() {
        assertFalse(afternoonCondition.isSatisfied());
        assertFalse(weekendCondition.isSatisfied());
    }

    /**
     * Tests what happens when the current time intervals are MORNING and AFTERNOON and the target
     * time interval is MORNING.
     */
    @Test
    public void testTargetInCurrentIntervals() {
        manager.sendUpdate(new TimeOfDayData(new int[]{TIME_INTERVAL_MORNING, TIME_INTERVAL_AFTERNOON}));
        assertTrue(afternoonCondition.isSatisfied());
    }

    /**
     * Tests what happens when the current time intervals are WEEKDAY and MORNING and the target
     * time intervals are EVENING and WEEKEND.
     */
    @Test
    public void testTargetNotInCurrentIntervals() {
        manager.sendUpdate(new TimeOfDayData(new int[]{TIME_INTERVAL_WEEKDAY, TIME_INTERVAL_MORNING}));
        assertFalse(weekendCondition.isSatisfied());
    }

}
