package uk.ac.strath.contextualtriggers.conditions;

import uk.ac.strath.contextualtriggers.Condition;

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
}
