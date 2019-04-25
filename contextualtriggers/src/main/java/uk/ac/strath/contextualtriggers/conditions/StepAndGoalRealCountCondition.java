package uk.ac.strath.contextualtriggers.conditions;

import java.time.LocalDate;

import uk.ac.strath.contextualtriggers.data.StepAndGoalData;
import uk.ac.strath.contextualtriggers.managers.IDataManager;

/**
 * A condition that compares the number of steps the user has taken today and their goal. It has two
 * modes, LESS_THAN and GREATER_THAN_OR_EQUAL_TO. In LESS_THAN mode, it is satisfied if the number
 * of steps the user has taken today is less than their goal. In GREATER_THAN_OR_EQUAL_TO mode, the
 * opposite is true.
 */
public class StepAndGoalRealCountCondition extends DataCondition<StepAndGoalData> {

    private int mode;

    public final static int LESS_THAN = 0;
    public final static int GREATER_THAN_OR_EQUAL_TO = 1;

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
        if (getData() == null) {
            return mode == LESS_THAN;
        }
        LocalDate today = LocalDate.now();
        if (getData().getDay(today).steps < 0) { // no step count updates received yet
            return false;
        }
        if (this.mode == LESS_THAN) {
            return (getData().getDay(today).steps < getData().getDay(today).goal);
        }
        if (this.mode == GREATER_THAN_OR_EQUAL_TO) {
            return (getData().getDay(today).steps >= getData().getDay(today).goal);
        }
        return false;
    }

}
