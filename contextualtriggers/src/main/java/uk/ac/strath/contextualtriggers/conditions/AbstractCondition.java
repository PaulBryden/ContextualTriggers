package uk.ac.strath.contextualtriggers.conditions;

import uk.ac.strath.contextualtriggers.Condition;
import uk.ac.strath.contextualtriggers.triggers.ITrigger;
import uk.ac.strath.contextualtriggers.triggers.Trigger;

/**
 * Base class for Condition implementations.
 */
public abstract class AbstractCondition implements Condition {

    private ITrigger trigger;

    public void attachTrigger(ITrigger trigger) {
        this.trigger = trigger;
    }

    ITrigger getTrigger() {
        if (trigger!=null)
        {
            return trigger;
        }else
        {
            throw new NullPointerException();
        }
    }
}
