package uk.ac.strath.contextualtriggers.conditions;

import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.junit.Test;

import uk.ac.strath.contextualtriggers.data.VoidData;
import uk.ac.strath.contextualtriggers.managers.DataManager;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FrequentNotificationPreventionConditionUnitTest {

    private class MockNotificationDataManager extends DataManager<VoidData>
    {
        @Nullable
        @Override
        public IBinder onBind(Intent intent)
        {
            return null;
        }

        public void sendMockUpdate(long timestamp) {
            this.sendUpdate(new VoidData(timestamp));
        }
    }

    @Test
    public void testConditionSatisfiedWhenNoNotificationSent() {
        MockNotificationDataManager manager = new MockNotificationDataManager();
        FrequentNotificationPreventionCondition condition = new FrequentNotificationPreventionCondition(10, manager);
        assertTrue(condition.isSatisfied());
    }


    @Test
    public void testConditionNotSatisfiedWhenNotificationSent() {
        MockNotificationDataManager manager = new MockNotificationDataManager();
        FrequentNotificationPreventionCondition condition = new FrequentNotificationPreventionCondition(10, manager);
        manager.sendMockUpdate(System.currentTimeMillis());
        assertFalse(condition.isSatisfied());
    }

    @Test
    public void testConditionSatisfiedWhenNotificationSentWithinTimeout() {
        MockNotificationDataManager manager = new MockNotificationDataManager();
        FrequentNotificationPreventionCondition condition = new FrequentNotificationPreventionCondition(10, manager);
        manager.sendMockUpdate(System.currentTimeMillis() - 9000);
        assertFalse(condition.isSatisfied());
    }


    @Test
    public void testConditionSatisfiedWhenNotificationSentBeforeTimeout() {
        MockNotificationDataManager manager = new MockNotificationDataManager();
        FrequentNotificationPreventionCondition condition = new FrequentNotificationPreventionCondition(10, manager);
        manager.sendMockUpdate(System.currentTimeMillis() - 20000);
        assertTrue(condition.isSatisfied());
    }


}
