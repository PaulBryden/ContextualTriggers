package uk.ac.strath.contextualtriggers;

public interface Action {

    /**
     * Method called by Trigger whenever the Trigger's Condition is satisfied.
     */
    void execute();

}
