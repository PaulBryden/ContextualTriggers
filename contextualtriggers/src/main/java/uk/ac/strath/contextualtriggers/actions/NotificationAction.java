package uk.ac.strath.contextualtriggers.actions;

import uk.ac.strath.contextualtriggers.Action;
import uk.ac.strath.contextualtriggers.Logger;

public class NotificationAction implements Action {

    private String message;
    private Logger logger;
    public NotificationAction(String message)
    {
        this.message = message;
        logger=Logger.getInstance();
    }

    @Override
    public void execute() {
        logger.log("*** SENDING NOTIFICATION ***\n\"" + message + "\"");
    }
}
