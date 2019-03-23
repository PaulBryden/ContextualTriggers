package uk.ac.strath.contextualtriggers.conditions;

import uk.ac.strath.contextualtriggers.data.StepData;

public class StepCountCondition extends DataCondition<StepData> {

    private int matchCount;
    private int mode;

    public final static int LESS_THAN = 0;
    public final static int GREATER_THAN = 1;

    public StepCountCondition(int mode, int count) {
        super();
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
        if (this.mode == GREATER_THAN) {
            return  getData().steps > matchCount;
        }
        return false;
    }
}
