package uk.ac.strath.contextualtriggers.conditions;

import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.junit.Test;

import uk.ac.strath.contextualtriggers.actions.UnitTestAction;
import uk.ac.strath.contextualtriggers.data.TimeOfDayData;
import uk.ac.strath.contextualtriggers.managers.DataManager;
import uk.ac.strath.contextualtriggers.managers.IDataManager;
import uk.ac.strath.contextualtriggers.triggers.Trigger;

import static com.google.android.gms.awareness.fence.TimeFence.TIME_INTERVAL_AFTERNOON;
import static com.google.android.gms.awareness.fence.TimeFence.TIME_INTERVAL_EVENING;
import static com.google.android.gms.awareness.fence.TimeFence.TIME_INTERVAL_MORNING;
import static com.google.android.gms.awareness.fence.TimeFence.TIME_INTERVAL_WEEKDAY;
import static com.google.android.gms.awareness.fence.TimeFence.TIME_INTERVAL_WEEKEND;
import static org.junit.Assert.assertEquals;

public class AcceptableTimeConditionUnitTest {

    /**
     * Tests what happens when the current time intervals are MORNING and AFTERNOON and the target
     * time interval is MORNING.
     */
    @Test
    public void AcceptableTimeConditionUnitTest() {
        class TimeIntervalsDataManager extends DataManager<TimeOfDayData> implements IDataManager<TimeOfDayData> {
            @Nullable
            @Override
            public IBinder onBind(Intent intent) {
                return null;
            }

            public void mock() {
                TimeOfDayData test = new TimeOfDayData(new int[]{TIME_INTERVAL_MORNING, TIME_INTERVAL_AFTERNOON});
                sendUpdate(test);
            }
        }

        UnitTestAction action = new UnitTestAction();
        TimeIntervalsDataManager manager = new TimeIntervalsDataManager();
        TimeOfDayData desiredTimeInterval = new TimeOfDayData(new int[]{TIME_INTERVAL_AFTERNOON});
        AcceptableTimeCondition periodCondition = new AcceptableTimeCondition(desiredTimeInterval, manager);
        Trigger.Builder T = new Trigger.Builder();
        T.setCondition(periodCondition);
        T.setAction(action);
        Trigger trig = T.build();
        manager.mock();
        assertEquals(true, periodCondition.isSatisfied());
        System.out.println("AcceptableTimeConditionUnitTest");
        manager.mock();
        assertEquals(true, periodCondition.isSatisfied());
    }

    /**
     * Tests what happens when the current time intervals are WEEKDAY and MORNING and the target
     * time intervals are EVENING and WEEKEND.
     */
    @Test
    public void AcceptableTimeConditionUnitTest2() {
        class TimeIntervalsDataManager extends DataManager<TimeOfDayData> implements IDataManager<TimeOfDayData> {
            @Nullable
            @Override
            public IBinder onBind(Intent intent) {
                return null;
            }

            public void mock() {
                TimeOfDayData test = new TimeOfDayData(new int[]{TIME_INTERVAL_WEEKDAY, TIME_INTERVAL_MORNING});
                sendUpdate(test);
            }
        }

        UnitTestAction action = new UnitTestAction();
        TimeIntervalsDataManager manager = new TimeIntervalsDataManager();
        TimeOfDayData desiredTimeInterval = new TimeOfDayData(new int[]{TIME_INTERVAL_EVENING, TIME_INTERVAL_WEEKEND});
        AcceptableTimeCondition periodCondition = new AcceptableTimeCondition(desiredTimeInterval, manager);
        Trigger.Builder T = new Trigger.Builder();
        T.setCondition(periodCondition);
        T.setAction(action);
        Trigger trig = T.build();
        manager.mock();
        assertEquals(false, periodCondition.isSatisfied());
        System.out.println("AcceptableTimeConditionUnitTest2");
        manager.mock();
        assertEquals(false, periodCondition.isSatisfied());
    }

}
