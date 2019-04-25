package uk.ac.strath.contextualtriggers.conditions;

import java.time.LocalDate;

import uk.ac.strath.contextualtriggers.data.DayData;
import uk.ac.strath.contextualtriggers.data.StepAndGoalData;
import uk.ac.strath.contextualtriggers.managers.IDataManager;

/**
 * A condition that is satisfied if the user failed to meet any of their recent goals.
 */
public class HistoricStepsDaysUnmetCondition extends DataCondition<StepAndGoalData> {

    private int daysMet;

    public HistoricStepsDaysUnmetCondition(int days, IDataManager<StepAndGoalData> dataManager) {
        super(dataManager);
        daysMet = days;
    }

    @Override
    public boolean hasStaleData() {
        return false;
    }

    @Override
    public boolean isSatisfied() {
        if (getData() == null) {
            return true;
        }
        DayData day;
        for (int i = 0; i < daysMet; i++) {
            day = getData().getDay(LocalDate.now().minusDays(i + 1));
            if (day == null || day.steps < day.goal) {
                return true;
            }
        }
        return false;
    }

}
