package uk.ac.strath.contextualtriggers.conditions;

import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.junit.Test;

import uk.ac.strath.contextualtriggers.actions.UnitTestAction;
import uk.ac.strath.contextualtriggers.data.StepAndGoalData;
import uk.ac.strath.contextualtriggers.managers.DataManager;
import uk.ac.strath.contextualtriggers.managers.IDataManager;
import uk.ac.strath.contextualtriggers.triggers.Trigger;

import static org.junit.Assert.assertEquals;

public class HistoricStepsDaysUnmetConditionUnitTest {

    /**
     * Tests what happens when the user has not met their goal.
     */
    @Test
    public void HistoricStepsDaysUnmetConditionUnitTest() {
        class StepAndGoalMockDataManager extends DataManager<StepAndGoalData> implements IDataManager<StepAndGoalData> {
            public StepAndGoalMockDataManager() {
                data = new StepAndGoalData();
            }

            boolean firstTime = true;
            StepAndGoalData data;

            @Nullable
            @Override
            public IBinder onBind(Intent intent) {
                return null;
            }

            public void mock() {
                sendUpdate(data);
            }
        }

        UnitTestAction action = new UnitTestAction();
        StepAndGoalMockDataManager manager = new StepAndGoalMockDataManager();
        HistoricStepsDaysUnmetCondition altTransCondition = new HistoricStepsDaysUnmetCondition(3, manager);
        Trigger.Builder T = new Trigger.Builder();
        T.setCondition(altTransCondition);
        T.setAction(action);
        Trigger trig = T.build();
        manager.mock();
        assertEquals(true, altTransCondition.isSatisfied());
        System.out.println("HistoricStepsDaysUnmetConditionUnitTest");
    }

}
