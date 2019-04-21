package uk.ac.strath.contextualtriggers.triggers;

import android.util.Log;

import uk.ac.strath.contextualtriggers.actions.Action;
import uk.ac.strath.contextualtriggers.conditions.Condition;
import uk.ac.strath.contextualtriggers.conditions.AbstractCondition;

/**
 * Triggers basically just link conditions to actions. If data relevant to the condition changes,
 * the Trigger checks if the condition is satisfied, and if so executes the action.
 */
public class Trigger implements ITrigger {

    private Condition condition;
    private Action action;

    Trigger(Condition condition, Action action) {
        this.condition = condition;
        this.action = action;
        ((AbstractCondition) this.condition).attachTrigger(this);
    }

    /**
     * Called by the condition (or any component condition) whenever new data is received
     * by that condition which might mean that the condition is satisfied.
     */
    public void notifyChange() {
        //Log.d("Trigger", String.format("Condition satisfied: %b", condition.isSatisfied()));
        if (condition.isSatisfied()) {
            action.execute();
        }
    }

    public static class Builder {

        private Condition condition;
        private Action action;

        public Builder setCondition(Condition condition) {
            this.condition = condition;
            return this;
        }

        public Builder setAction(Action action) {
            this.action = action;
            return this;
        }

        public Trigger build() {
            return new Trigger(condition, action);
        }

    }

}
