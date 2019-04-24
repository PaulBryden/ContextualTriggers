package uk.ac.strath.contextualtriggers.conditions;

import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.android.gms.location.DetectedActivity;

import org.junit.Test;

import uk.ac.strath.contextualtriggers.actions.UnitTestAction;
import uk.ac.strath.contextualtriggers.data.ActivityData;
import uk.ac.strath.contextualtriggers.managers.DataManager;
import uk.ac.strath.contextualtriggers.managers.IDataManager;
import uk.ac.strath.contextualtriggers.triggers.Trigger;

import static com.google.android.gms.location.DetectedActivity.STILL;
import static org.junit.Assert.assertEquals;

public class ActivityPeriodConditionUnitTest {

    @Test
    public void ActivityPeriodConditionUnitTest() {
        class ActivityDataManager extends DataManager<ActivityData> implements IDataManager<ActivityData> {
            @Nullable
            @Override
            public IBinder onBind(Intent intent) {
                return null;
            }

            public void mock() {
                sendUpdate(new ActivityData(new DetectedActivity(STILL, 100)));
            }
        }

        UnitTestAction action = new UnitTestAction();
        ActivityDataManager manager = new ActivityDataManager();
        ActivityPeriodCondition periodCondition = new ActivityPeriodCondition(10000, STILL, manager);
        Trigger.Builder T = new Trigger.Builder();
        T.setCondition(periodCondition);
        T.setAction(action);
        Trigger trig = T.build();
        manager.mock();
        assertEquals(false, periodCondition.isSatisfied());
        System.out.println("ActivityPeriodConditionUnitTest");
        try {
            Thread.sleep(10500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        manager.mock();
        assertEquals(true, periodCondition.isSatisfied());
    }

}
