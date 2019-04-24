package uk.ac.strath.contextualtriggers.actions;

import android.content.Context;

import uk.ac.strath.contextualtriggers.conditions.FrequentNotificationPreventionCondition;

//import static android.support.v4.content.ContextCompat.getSystemService;

public class UnitTestAction implements Action {

    private static final String CHANNEL_ID = "contextualtriggers";
    private String message;
    private FrequentNotificationPreventionCondition notifyCondition;
    private Context ct;

    public UnitTestAction() {
        this.message = message;
        createNotificationChannel();
    }



    @Override
    public void execute() {

    }

    private void createNotificationChannel() {

    }

    public void attachCondition(FrequentNotificationPreventionCondition notifyCondition) {
        this.notifyCondition = notifyCondition;
    }

}
