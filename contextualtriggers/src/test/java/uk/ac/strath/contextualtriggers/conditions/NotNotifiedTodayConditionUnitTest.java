package uk.ac.strath.contextualtriggers.conditions;

import org.junit.Before;
import org.junit.Test;

import uk.ac.strath.contextualtriggers.data.VoidData;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NotNotifiedTodayConditionUnitTest {

    private MockDataManager<VoidData> manager;
    private NotNotifiedTodayCondition condition;

    @Before
    public void setup() {
        manager = new MockDataManager<>();
        condition = new NotNotifiedTodayCondition(manager);
    }

    @Test
    public void testConditionSatisfied() {
        assertTrue(condition.isSatisfied());
    }

    @Test
    public void testConditionNotSatisfied() {
        manager.sendUpdate(new VoidData());
        assertFalse(condition.isSatisfied());
    }

}
