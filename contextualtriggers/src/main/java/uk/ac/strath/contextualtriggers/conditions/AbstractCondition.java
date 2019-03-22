package uk.ac.strath.contextualtriggers.conditions;

import uk.ac.strath.contextualtriggers.Condition;
import uk.ac.strath.contextualtriggers.Trigger;

/**
 * Base class for Condition implementations.
 */
public abstract class AbstractCondition implements Condition {

    private Trigger trigger;

    public void attachTrigger(Trigger trigger) {
        this.trigger = trigger;
    }

    Trigger getTrigger() {
        return trigger;
    }
}
