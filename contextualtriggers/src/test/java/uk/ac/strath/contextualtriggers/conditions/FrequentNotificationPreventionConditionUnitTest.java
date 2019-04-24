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
        FrequentNotificationPreventionCondition altTransCondition = new FrequentNotificationPreventionCondition(10000,manager);
        Trigger.Builder T = new Trigger.Builder();
        T.setCondition(altTransCondition);
        T.setAction(action);
        Trigger trig = T.build();
        assertEquals(true,altTransCondition.isSatisfied());
        System.out.println("FrequentNotificationPreventionConditionUnitTest");
        manager.mock();
        assertEquals(false,altTransCondition.isSatisfied());
        try
        {
            Thread.sleep(10500);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        assertEquals(true,altTransCondition.isSatisfied());
        manager.mock();
        assertEquals(false,altTransCondition.isSatisfied());

    }

}
