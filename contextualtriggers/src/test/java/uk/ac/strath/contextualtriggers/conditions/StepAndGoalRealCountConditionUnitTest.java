package uk.ac.strath.contextualtriggers.conditions;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import uk.ac.strath.contextualtriggers.data.DayData;
import uk.ac.strath.contextualtriggers.data.StepAndGoalData;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static uk.ac.strath.contextualtriggers.conditions.StepAndGoalRealCountCondition.GREATER_THAN_OR_EQUAL_TO;
import static uk.ac.strath.contextualtriggers.conditions.StepAndGoalRealCountCondition.LESS_THAN;

public class StepAndGoalRealCountConditionUnitTest {

    private MockDataManager<StepAndGoalData> manager;
    private StepAndGoalRealCountCondition lessThanCondition;
    private StepAndGoalRealCountCondition greaterThanCondition;
    private StepAndGoalData lowStepCountData;
    private StepAndGoalData highStepCountData;

    @Before
    public void setup() {
        manager = new MockDataManager<>();
        lessThanCondition = new StepAndGoalRealCountCondition(LESS_THAN, manager);
        greaterThanCondition = new StepAndGoalRealCountCondition(GREATER_THAN_OR_EQUAL_TO, manager);
        lowStepCountData = new StepAndGoalData();
        lowStepCountData.updateDay(new DayData(10000, 12000, LocalDate.now()));
        highStepCountData = new StepAndGoalData();
        highStepCountData.updateDay(new DayData(15000, 12000, LocalDate.now()));
    }

    @Test
    public void testNoDataReceivedYet() {
        assertTrue(lessThanCondition.isSatisfied());
        assertFalse(greaterThanCondition.isSatisfied());
    }

    @Test
    public void testLowStepCount() {
        manager.sendUpdate(lowStepCountData);
        assertTrue(lessThanCondition.isSatisfied());
        assertFalse(greaterThanCondition.isSatisfied());
    }

    @Test
    public void testHighStepCount() {
        manager.sendUpdate(highStepCountData);
        assertFalse(lessThanCondition.isSatisfied());
        assertTrue(greaterThanCondition.isSatisfied());
    }

}
