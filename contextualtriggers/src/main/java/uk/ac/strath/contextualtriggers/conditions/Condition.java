package uk.ac.strath.contextualtriggers.conditions;

public interface Condition {

    /**
     * @return true iff the condition is satisfied.
     */
    boolean isSatisfied();

    /**
     * @return true iff condition should not be considered satisfied because data is too old.
     */
    boolean hasStaleData();
}
