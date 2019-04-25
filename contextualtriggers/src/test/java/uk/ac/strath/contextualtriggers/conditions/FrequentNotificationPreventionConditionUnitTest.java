package uk.ac.strath.contextualtriggers.conditions;

import org.junit.Test;

import uk.ac.strath.contextualtriggers.data.VoidData;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FrequentNotificationPreventionConditionUnitTest {

    @Test
    public void testConditionSatisfiedWhenNoNotificationSent() {
        MockDataManager<VoidData> manager = new MockDataManager<>();
        FrequentNotificationPreventionCondition condition = new FrequentNotificationPreventionCondition(10, manager);
        assertTrue(condition.isSatisfied());
    }

    @Test
    public void testConditionNotSatisfiedWhenNotificationSent() {
        MockDataManager<VoidData> manager = new MockDataManager<>();
        FrequentNotificationPreventionCondition condition = new FrequentNotificationPreventionCondition(10, manager);
        manager.sendUpdate(new VoidData(System.currentTimeMillis()));
        assertFalse(condition.isSatisfied());
    }

    @Test
    public void testConditionSatisfiedWhenNotificationSentWithinTimeout() {
        MockDataManager<VoidData> manager = new MockDataManager<>();
        FrequentNotificationPreventionCondition condition = new FrequentNotificationPreventionCondition(10, manager);
        manager.sendUpdate(new VoidData(System.currentTimeMillis() - 9000));
        assertFalse(condition.isSatisfied());
    }


    @Test
    public void testConditionSatisfiedWhenNotificationSentBeforeTimeout() {
        MockDataManager<VoidData> manager = new MockDataManager<>();
        FrequentNotificationPreventionCondition condition = new FrequentNotificationPreventionCondition(10, manager);
        manager.sendUpdate(new VoidData(System.currentTimeMillis() - 11000));
        assertTrue(condition.isSatisfied());
    }

}
