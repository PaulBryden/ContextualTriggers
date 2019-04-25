package uk.ac.strath.contextualtriggers.conditions;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Date;

import uk.ac.strath.contextualtriggers.data.CalendarData;
import uk.ac.strath.contextualtriggers.data.EventData;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MeetingConditionUnitTest {

    private MockDataManager<CalendarData> manager;
    private MeetingCondition condition;
    private final EventData meeting = new EventData("Meeting with Paul", new Date(System.currentTimeMillis() + 60*60*1000));
    private final EventData laterMeeting = new EventData("Meeting with Paul", new Date(System.currentTimeMillis() + 3*24*60*60*1000));
    private final EventData party = new EventData("Party time!", new Date(System.currentTimeMillis() + 60*60*1000));

    @Before
    public void setup() {
        manager = new MockDataManager<>();
        condition = new MeetingCondition(manager);
    }

    @Test
    public void testNoDataReceivedYet() {
        assertFalse(condition.isSatisfied());
    }

    /**
     * Tests what happens when the user has a meeting in 1 hour.
     */
    @Test
    public void testMeetingInOneHour() {
        manager.sendUpdate(new CalendarData(Collections.singletonList(meeting)));
        assertTrue(condition.isSatisfied());
    }

    /**
     * Tests what happens when the user does not have any meetings in the next 2 hours.
     */
    @Test
    public void testPartyInOneHour() {
        manager.sendUpdate(new CalendarData(Collections.singletonList(party)));
        assertFalse(condition.isSatisfied());
    }

    /**
     * Tests what happens when the user has a meeting in a couple of days.
     */
    @Test
    public void testMeetingInThreeDays() {
        manager.sendUpdate(new CalendarData(Collections.singletonList(laterMeeting)));
        assertFalse(condition.isSatisfied());
    }

}
