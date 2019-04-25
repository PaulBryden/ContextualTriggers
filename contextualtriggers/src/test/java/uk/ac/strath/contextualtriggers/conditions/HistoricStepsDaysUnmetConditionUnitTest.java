package uk.ac.strath.contextualtriggers.conditions;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import uk.ac.strath.contextualtriggers.data.DayData;
import uk.ac.strath.contextualtriggers.data.StepAndGoalData;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HistoricStepsDaysUnmetConditionUnitTest {

    private MockDataManager<StepAndGoalData> manager;
    private HistoricStepsDaysUnmetCondition condition;

    @Before
    public void setup() {
        manager = new MockDataManager<>();
        condition = new HistoricStepsDaysUnmetCondition(3, manager);
    }

    @Test
    public void testNoDataReceivedYet() {
        assertTrue(condition.isSatisfied());
    }

    @Test
    public void testEmptyHistoryReceived() {
        manager.sendUpdate(new StepAndGoalData());
        assertTrue(condition.isSatisfied());
    }

    @Test
    public void testConditionNotSatisfiedAsGoalsMet() {
        StepAndGoalData data = new StepAndGoalData();
        data.updateDay(new DayData(600, 500, LocalDate.now()));
        data.updateDay(new DayData(600, 500, LocalDate.now().minusDays(1)));
        data.updateDay(new DayData(600, 500, LocalDate.now().minusDays(2)));
        data.updateDay(new DayData(600, 500, LocalDate.now().minusDays(3)));
        manager.sendUpdate(data);
        assertFalse(condition.isSatisfied());
    }

    @Test
    public void testConditionNotSatisfiedAsGoalsMetExceptToday() {
        StepAndGoalData data = new StepAndGoalData();
        data.updateDay(new DayData(300, 500, LocalDate.now()));
        data.updateDay(new DayData(600, 500, LocalDate.now().minusDays(1)));
        data.updateDay(new DayData(600, 500, LocalDate.now().minusDays(2)));
        data.updateDay(new DayData(600, 500, LocalDate.now().minusDays(3)));
        manager.sendUpdate(data);
        assertFalse(condition.isSatisfied());
    }

    @Test
    public void testConditionSatisfiedAsGoalNotMet() {
        StepAndGoalData data = new StepAndGoalData();
        data.updateDay(new DayData(600, 500, LocalDate.now()));
        data.updateDay(new DayData(600, 500, LocalDate.now().minusDays(1)));
        data.updateDay(new DayData(600, 500, LocalDate.now().minusDays(2)));
        data.updateDay(new DayData(300, 500, LocalDate.now().minusDays(3)));
        manager.sendUpdate(data);
        assertTrue(condition.isSatisfied());
    }

}
