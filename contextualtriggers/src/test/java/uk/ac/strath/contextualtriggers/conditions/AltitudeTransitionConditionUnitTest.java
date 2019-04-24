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
        AltitudeTransitionCondition altTransCondition = new AltitudeTransitionCondition(19, manager);
        Trigger.Builder T = new Trigger.Builder();
        T.setCondition(altTransCondition);
        T.setAction(action);
        Trigger trig = T.build();
        manager.mock();
        assertEquals(false, altTransCondition.isSatisfied());
        System.out.println("AcceptableTimeConditionUnitTest");
        manager.mock();
        assertEquals(true, altTransCondition.isSatisfied());
    }

}
