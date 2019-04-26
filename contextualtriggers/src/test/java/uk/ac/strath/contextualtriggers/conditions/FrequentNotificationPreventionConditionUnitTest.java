package uk.ac.strath.contextualtriggers.conditions;

import org.junit.Before;
import org.junit.Test;

import uk.ac.strath.contextualtriggers.data.VoidData;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FrequentNotificationPreventionConditionUnitTest {

    private MockDataManager<VoidData> manager;
    private FrequentNotificationPreventionCondition condition;

    @Before
    public void setup() {
        manager = new MockDataManager<>();
        condition = new FrequentNotificationPreventionCondition(10, manager);
    }

    @Test
    public void testConditionSatisfiedWhenNoNotificationSent() {
        assertTrue(condition.isSatisfied());
    }

    @Test
    public void testConditionNotSatisfiedWhenNotificationSent() {
        manager.sendUpdate(new VoidData(System.currentTimeMillis()));
        assertFalse(condition.isSatisfied());
    }

    @Test
    public void testConditionSatisfiedWhenNotificationSentWithinTimeout() {
        manager.sendUpdate(new VoidData(System.currentTimeMillis() - 9000));
        assertFalse(condition.isSatisfied());
    }


    @Test
    public void testConditionSatisfiedWhenNotificationSentBeforeTimeout() {
        manager.sendUpdate(new VoidData(System.currentTimeMillis() - 11000));
        assertTrue(condition.isSatisfied());
    }

}
