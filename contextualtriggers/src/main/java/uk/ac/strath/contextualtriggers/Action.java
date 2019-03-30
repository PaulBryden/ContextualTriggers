package uk.ac.strath.contextualtriggers;

import android.app.Service;

public interface Action{

    /**
     * Method called by Trigger whenever the Trigger's Condition is satisfied.
     */
    void execute();

}
