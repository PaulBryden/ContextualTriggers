package uk.ac.strath.contextualtriggers.conditions;

import uk.ac.strath.contextualtriggers.data.StepData;
import uk.ac.strath.contextualtriggers.managers.IDataManager;

/**
 * A condition that compares the number of steps the user has taken today and the target. It has two
 * modes, LESS_THAN and GREATER_THAN_OR_EQUAL_TO. In LESS_THAN mode, it is satisfied if the number
 * of steps the user has taken today is less than their goal. In GREATER_THAN_OR_EQUAL_TO mode, the
 * opposite is true.
 */
public class StepCountCondition extends DataCondition<StepData> {

    private int matchCount;
    private int mode;

    public final static int LESS_THAN = 0;
    public final static int GREATER_THAN_OR_EQUAL_TO = 1;

    public StepCountCondition(int mode, int count, IDataManager<StepData> dataManager) {
        super(dataManager);
        this.mode = mode;
        this.matchCount = count;
    }

    @Override
    public boolean isSatisfied() {
        if (getData().steps < 0) { // no step count updates received yet
            return false;
        }
        if (this.mode == LESS_THAN) {
            return getData().steps < matchCount;
        }
        if (this.mode == GREATER_THAN_OR_EQUAL_TO) {
            return getData().steps >= matchCount;
        }
        return false;
    }

}
