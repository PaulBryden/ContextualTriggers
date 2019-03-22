package uk.ac.strath.contextualtriggers;

import uk.ac.strath.contextualtriggers.conditions.AbstractCondition;

/**
 * Triggers basically just link conditions to actions. If data relevant to the condition changes,
 * the Trigger checks if the condition is satisfied, and if so executes the action.
 */
public class Trigger {

    private Condition condition;
    private Action action;

    public Trigger(Condition condition, Action action) {
        this.condition = condition;
        this.action = action;
        ((AbstractCondition) this.condition).attachTrigger(this);
    }

    /**
     * Called by the condition (or any component condition) whenever new data is received
     * by that condition which might mean that the condition is satisfied.
     */
    public void notifyChange() {
        if (condition.isSatisfied()) {
            action.execute();
        }
    }

}
