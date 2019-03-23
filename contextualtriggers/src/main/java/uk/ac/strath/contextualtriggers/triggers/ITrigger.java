package uk.ac.strath.contextualtriggers.triggers;

public interface ITrigger{

    /**
     * Called by the condition (or any component condition) whenever new data is received
     * by that condition which might mean that the condition is satisfied.
     */
    public void notifyChange();
}
