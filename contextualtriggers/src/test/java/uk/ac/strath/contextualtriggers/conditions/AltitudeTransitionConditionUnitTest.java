package uk.ac.strath.contextualtriggers.conditions;

import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.junit.Test;

import uk.ac.strath.contextualtriggers.actions.UnitTestAction;
import uk.ac.strath.contextualtriggers.data.AltitudeData;
import uk.ac.strath.contextualtriggers.managers.DataManager;
import uk.ac.strath.contextualtriggers.managers.IDataManager;
import uk.ac.strath.contextualtriggers.triggers.Trigger;

import static org.junit.Assert.assertEquals;

public class AltitudeTransitionConditionUnitTest {

    /**
     * Tests what happens when the user's altitude increases.
     */
    @Test
    public void AltitudeTransitionConditionUnitTest() {
        class AltitudeMockManager extends DataManager<AltitudeData> implements IDataManager<AltitudeData> {
            boolean firstTime = true;

            @Nullable
            @Override
            public IBinder onBind(Intent intent) {
                return null;
            }

            public void mock() {
                AltitudeData data = new AltitudeData();
                if (firstTime) {
                    data.altitude = 0;
                    firstTime = false;
                } else {
                    data.altitude = 20;
                }
                sendUpdate(data);
            }
        }

        UnitTestAction action = new UnitTestAction();
        AltitudeMockManager manager = new AltitudeMockManager();
        AltitudeTransitionCondition condition = new AltitudeTransitionCondition(19, manager);
        new Trigger.Builder().setCondition(condition).setAction(action).build();
        manager.mock();
        assertEquals(false, condition.isSatisfied());
        System.out.println("AcceptableTimeConditionUnitTest");
        manager.mock();
        assertEquals(true, condition.isSatisfied());
    }

    /**
     * Tests what happens when the user's altitude decreases.
     */
    @Test
    public void AltitudeTransitionConditionUnitTest2() {
        class AltitudeMockManager extends DataManager<AltitudeData> implements IDataManager<AltitudeData> {
            boolean firstTime = true;

            @Nullable
            @Override
            public IBinder onBind(Intent intent) {
                return null;
            }

            public void mock() {
                AltitudeData data = new AltitudeData();
                if (firstTime) {
                    data.altitude = 0;
                    firstTime = false;
                } else {
                    data.altitude = -20;
                }
                sendUpdate(data);
            }
        }

        UnitTestAction action = new UnitTestAction();
        AltitudeMockManager manager = new AltitudeMockManager();
        AltitudeTransitionCondition condition = new AltitudeTransitionCondition(19, manager);
        new Trigger.Builder().setCondition(condition).setAction(action).build();
        manager.mock();
        assertEquals(false, condition.isSatisfied());
        System.out.println("AcceptableTimeConditionUnitTest2");
        manager.mock();
        assertEquals(false, condition.isSatisfied());
    }

    /**
     * Tests what happens when the user's altitude increases too slowly.
     */
    @Test
    public void AltitudeTransitionConditionUnitTest3() {
        class AltitudeMockManager extends DataManager<AltitudeData> implements IDataManager<AltitudeData> {
            boolean firstTime = true;

            @Nullable
            @Override
            public IBinder onBind(Intent intent) {
                return null;
            }

            public void mock() {
                AltitudeData data = new AltitudeData();
                if (firstTime) {
                    data.altitude = 0;
                    firstTime = false;
                } else {
                    data.altitude = 10;
                }
                sendUpdate(data);
            }
        }

        UnitTestAction action = new UnitTestAction();
        AltitudeMockManager manager = new AltitudeMockManager();
        AltitudeTransitionCondition condition = new AltitudeTransitionCondition(19, manager);
        new Trigger.Builder().setCondition(condition).setAction(action).build();
        manager.mock();
        assertEquals(false, condition.isSatisfied());
        System.out.println("AcceptableTimeConditionUnitTest3");
        manager.mock();
        assertEquals(false, condition.isSatisfied());
    }

}
