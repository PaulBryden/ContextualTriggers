package uk.ac.strath.contextualtriggers.conditions;

import java.time.LocalDate;

import uk.ac.strath.contextualtriggers.data.StepAndGoalData;
import uk.ac.strath.contextualtriggers.managers.IDataManager;

public class StepAndGoalRealCountCondition extends DataCondition<StepAndGoalData> {

    private int mode;

    public final static int LESS_THAN = 0;
    public final static int GREATER_THAN = 1;

    public StepAndGoalRealCountCondition(int mode, IDataManager<StepAndGoalData> dataManager) {
        super(dataManager);
        this.mode = mode;
    }

    @Override
    public boolean hasStaleData() {
        return false;
    }

    @Override
    public boolean isSatisfied() {
        LocalDate today = LocalDate.now();
        if (getData().getDay(today).steps < 0) { // no step count updates received yet
            return false;
        }
        if (this.mode == LESS_THAN) {
            return (getData().getDay(today).steps < getData().getDay(today).goal);
        }
        if (this.mode == GREATER_THAN) {
            return  (getData().getDay(today).steps > getData().getDay(today).goal);
        }
        return false;
    }
}
