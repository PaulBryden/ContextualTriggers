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

public class FrequentNotificationPreventionConditionUnitTest {

    /**
     * Tests what happens immediately after, and 10.5 seconds after, the user is notified.
     */
    @Test
    public void FrequentNotificationPreventionConditionUnitTest() {
        class NotificationMockDataManager extends DataManager<VoidData> implements IDataManager<VoidData>
        {
            boolean firstTime=true;
            @Nullable
            @Override
            public IBinder onBind(Intent intent)
            {
                return null;
            }
            public void mock()
            {
                sendUpdate(null);
            }
        }

        UnitTestAction action = new UnitTestAction();
        NotificationMockDataManager manager = new NotificationMockDataManager();
        FrequentNotificationPreventionCondition condition = new FrequentNotificationPreventionCondition(10000,manager);
        new Trigger.Builder().setCondition(condition).setAction(action).build();
        assertEquals(true,condition.isSatisfied());
        System.out.println("FrequentNotificationPreventionConditionUnitTest");
        manager.mock();
        assertEquals(false,condition.isSatisfied());
        try
        {
            Thread.sleep(10500);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        assertEquals(true,condition.isSatisfied());
        manager.mock();
        assertEquals(false,condition.isSatisfied());

    }

}
