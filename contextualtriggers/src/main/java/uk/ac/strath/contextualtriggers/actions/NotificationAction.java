package uk.ac.strath.contextualtriggers.actions;

import uk.ac.strath.contextualtriggers.Action;

public class NotificationAction implements Action {

    private String message;

    public NotificationAction(String message) {
        this.message = message;
    }

    @Override
    public void execute() {
        System.out.println("*** SENDING NOTIFICATION ***\n\"" + message + "\"");
    }
}
