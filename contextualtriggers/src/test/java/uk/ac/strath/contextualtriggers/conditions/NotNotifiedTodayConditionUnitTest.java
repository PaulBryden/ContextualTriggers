package uk.ac.strath.contextualtriggers.conditions;

import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.junit.Test;

import uk.ac.strath.contextualtriggers.actions.UnitTestAction;
import uk.ac.strath.contextualtriggers.data.VoidData;
import uk.ac.strath.contextualtriggers.managers.DataManager;
import uk.ac.strath.contextualtriggers.managers.IDataManager;
import uk.ac.strath.contextualtriggers.triggers.Trigger;

import static org.junit.Assert.assertEquals;

public class NotNotifiedTodayConditionUnitTest {

    /**
     * Tests what happens when the user has not, and has, been notified today.
     */
    @Test
    public void NotNotifiedTodayConditionUnitTest() {
        class MockNotificationDataManager extends DataManager<VoidData> implements IDataManager<VoidData> {
            public MockNotificationDataManager() {
            }

            @Nullable
            @Override
            public IBinder onBind(Intent intent) {
                return null;
            }

            public void mock() {
                sendUpdate(new VoidData());
            }
        }

        UnitTestAction action = new UnitTestAction();
        MockNotificationDataManager manager = new MockNotificationDataManager();
        NotNotifiedTodayCondition condition = new NotNotifiedTodayCondition(manager);
        new Trigger.Builder().setCondition(condition).setAction(action).build();
        assertEquals(true, condition.isSatisfied());
        manager.mock();
        assertEquals(false, condition.isSatisfied());
        System.out.println("NotNotifiedTodayConditionUnitTest");
    }

}
