package uk.ac.strath.contextualtriggers.conditions;

import java.util.List;

public class AndCondition extends CompositeCondition {

    public AndCondition(List<Condition> components) {
        super(components);
    }

    @Override
    public boolean isSatisfied() {
        for (Condition c : components) {
            if (!c.isSatisfied()) {
                return false;
            }
        }
        return true;
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
