package uk.ac.strath.contextualtriggers.conditions;

import uk.ac.strath.contextualtriggers.exceptions.TriggerNotConnectedException;
import uk.ac.strath.contextualtriggers.triggers.ITrigger;

/**
 * Base class for Condition implementations.
 */
public abstract class AbstractCondition implements Condition {

    private ITrigger trigger;

    public void attachTrigger(ITrigger trigger) {
        this.trigger = trigger;
    }

    ITrigger getTrigger() throws TriggerNotConnectedException{
        if (trigger!=null)
        {
            return trigger;
        }else
        {
            throw new TriggerNotConnectedException();
        }
    }
}
