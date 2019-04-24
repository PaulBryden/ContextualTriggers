package uk.ac.strath.contextualtriggers.conditions;

import java.util.List;

import uk.ac.strath.contextualtriggers.triggers.ITrigger;

/**
 * Base class for composite conditions (conditions that depend on multiple other conditions,
 * such as AndCondition).
 */
public abstract class CompositeCondition extends AbstractCondition {

    List<Condition> components;

    @Override
    public void attachTrigger(ITrigger trigger) {
        super.attachTrigger(trigger);
        for (Condition condition : components) {
            ((AbstractCondition) condition).attachTrigger(trigger);
        }
    }

    CompositeCondition(List<Condition> components) {
        this.components = components;
    }

}
