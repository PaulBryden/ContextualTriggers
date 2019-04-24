package uk.ac.strath.contextualtriggers.conditions;

import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.junit.Test;

import java.time.LocalDate;

import uk.ac.strath.contextualtriggers.actions.UnitTestAction;
import uk.ac.strath.contextualtriggers.data.DayData;
import uk.ac.strath.contextualtriggers.data.StepAndGoalData;
import uk.ac.strath.contextualtriggers.managers.DataManager;
import uk.ac.strath.contextualtriggers.managers.IDataManager;
import uk.ac.strath.contextualtriggers.triggers.Trigger;

import static org.junit.Assert.assertEquals;
import static uk.ac.strath.contextualtriggers.conditions.StepAndGoalRealCountCondition.LESS_THAN;

public class StepAndGoalRealCountConditionUnitTest {

    @Test
    public void StepAndGoalRealCountConditionUnitTest() {
        class MockStepAndGoalCountManager extends DataManager<StepAndGoalData> implements IDataManager<StepAndGoalData> {

            public MockStepAndGoalCountManager() {
                firstTime = true;
            }

            boolean firstTime;

            @Nullable
            @Override
            public IBinder onBind(Intent intent) {
                return null;
            }

            public void mock() {
                if (firstTime) {
                    sendUpdate(new StepAndGoalData());
                    firstTime = false;
                } else {
                    StepAndGoalData fulfilled = new StepAndGoalData();
                    LocalDate today = LocalDate.now();
                    DayData day = fulfilled.getDay(today);
                    day.steps = 100000;
                    fulfilled.updateDay(day);
                    sendUpdate(fulfilled);
                }
            }
        }

        UnitTestAction action = new UnitTestAction();
        MockStepAndGoalCountManager manager = new MockStepAndGoalCountManager();
        StepAndGoalRealCountCondition altTransCondition = new StepAndGoalRealCountCondition(LESS_THAN, manager);
        Trigger.Builder T = new Trigger.Builder();
        T.setCondition(altTransCondition);
        T.setAction(action);
        Trigger trig = T.build();
        manager.mock();
        assertEquals(true, altTransCondition.isSatisfied());
        manager.mock();
        assertEquals(false, altTransCondition.isSatisfied());
        System.out.println("NotNotifiedTodayConditionUnitTest");
    }

}
