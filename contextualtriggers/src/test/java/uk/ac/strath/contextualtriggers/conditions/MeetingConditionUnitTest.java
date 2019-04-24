package uk.ac.strath.contextualtriggers.conditions;

import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import uk.ac.strath.contextualtriggers.actions.UnitTestAction;
import uk.ac.strath.contextualtriggers.data.CalendarData;
import uk.ac.strath.contextualtriggers.data.EventData;
import uk.ac.strath.contextualtriggers.managers.DataManager;
import uk.ac.strath.contextualtriggers.managers.IDataManager;
import uk.ac.strath.contextualtriggers.triggers.Trigger;

import static org.junit.Assert.assertTrue;

public class MeetingConditionUnitTest {

    @Test
    public void MeetingConditionUnitTest() {
        class MockCalendarDataManager extends DataManager<CalendarData> implements IDataManager<CalendarData> {
            @Nullable
            @Override
            public IBinder onBind(Intent intent) {
                return null;
            }

            public void mock() {
                List<EventData> data = new ArrayList<>();
                Calendar c = Calendar.getInstance();
                c.add(Calendar.HOUR, 1);
                Date t = c.getTime();
                // Schedule a meeting in 1 h.
                data.add(new EventData("Meeting", t));
                sendUpdate(new CalendarData(data));
            }
        }
        MockCalendarDataManager manager = new MockCalendarDataManager();
        MeetingCondition condition = new MeetingCondition(manager);
        UnitTestAction action = new UnitTestAction();
        Trigger trigger = new Trigger.Builder().setCondition(condition).setAction(action).build();
        manager.mock();
        assertTrue(condition.isSatisfied());
        System.out.println("MeetingConditionUnitTest");
    }

}
