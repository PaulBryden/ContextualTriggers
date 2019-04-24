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
        NotNotifiedTodayCondition altTransCondition = new NotNotifiedTodayCondition(manager);
        Trigger.Builder T = new Trigger.Builder();
        T.setCondition(altTransCondition);
        T.setAction(action);
        Trigger trig = T.build();
        assertEquals(true, altTransCondition.isSatisfied());
        manager.mock();
        assertEquals(false, altTransCondition.isSatisfied());
        System.out.println("NotNotifiedTodayConditionUnitTest");
    }

}
