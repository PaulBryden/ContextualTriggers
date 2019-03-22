package uk.ac.strath.contextualtriggers.conditions;

public class StepCountCondition extends DataCondition<Integer> {

    private int matchCount;
    private int mode;

    public final static int LESS_THAN = 0;
    public final static int GREATER_THAN = 1;

    public StepCountCondition(int mode, int count) {
        super(-1);
        this.mode = mode;
        this.matchCount = count;
    }

    @Override
    public boolean isSatisfied() {
        if (getData() < 0) { // no step count updates received yet
            return false;
        }
        if (this.mode == LESS_THAN) {
            return getData() < matchCount;
        }
        if (this.mode == GREATER_THAN) {
            return  getData() > matchCount;
        }
        return false;
    }
}
