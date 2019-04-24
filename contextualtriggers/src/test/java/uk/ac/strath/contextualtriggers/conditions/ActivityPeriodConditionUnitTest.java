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
import static com.google.android.gms.location.DetectedActivity.UNKNOWN;
import static org.junit.Assert.assertEquals;

public class ActivityPeriodConditionUnitTest {

    /**
     * Tests what happens when the user has been still for 10 seconds.
     */
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

    /**
     * Checks what happens when the user has not been still for 10 seconds because they have only
     * been still for 5 seconds.
     */
    @Test
    public void ActivityPeriodConditionUnitTest2() {
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
        System.out.println("ActivityPeriodConditionUnitTest2");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        manager.mock();
        assertEquals(false, periodCondition.isSatisfied());
    }

    /**
     * Tests what happens when the user has not been still for 10 seconds because their activity is
     * unknown.
     */
    @Test
    public void ActivityPeriodConditionUnitTest3() {
        class ActivityDataManager extends DataManager<ActivityData> implements IDataManager<ActivityData> {
            @Nullable
            @Override
            public IBinder onBind(Intent intent) {
                return null;
            }

            public void mock() {
                sendUpdate(new ActivityData(new DetectedActivity(UNKNOWN, 100)));
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
        System.out.println("ActivityPeriodConditionUnitTest3");
        try {
            Thread.sleep(10500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        manager.mock();
        assertEquals(false, periodCondition.isSatisfied());
    }

}
