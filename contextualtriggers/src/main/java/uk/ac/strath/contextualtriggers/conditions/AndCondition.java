package uk.ac.strath.contextualtriggers.conditions;

import android.util.Log;

import java.util.List;

public class AndCondition extends CompositeCondition {

    public AndCondition(List<Condition> components) {
        super(components);
    }

    @Override
    public boolean isSatisfied() {
        boolean satisfied = true;
        for (Condition c : components) {
            Log.d("Condition", c.getClass().getSimpleName() + " satisfied: " + c.isSatisfied());
            if (!c.isSatisfied()) {
                satisfied = false;
            }
        }
        Log.d("Condition", "AndCondition with " + components.size() + " components satisfied: " + satisfied);
        return satisfied;
    }

    @Override
    public boolean hasStaleData() {
        for (Condition c : components) {
            if (c.hasStaleData()) {
                return true;
            }
        }
        return false;
    }

}
