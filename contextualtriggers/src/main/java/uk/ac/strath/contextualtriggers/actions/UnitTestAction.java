package uk.ac.strath.contextualtriggers.actions;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import uk.ac.strath.contextualtriggers.Logger;
import uk.ac.strath.contextualtriggers.MainApplication;
import uk.ac.strath.contextualtriggers.R;
import uk.ac.strath.contextualtriggers.conditions.FrequentNotificationPreventionCondition;
import uk.ac.strath.contextualtriggers.managers.NotificationDataManager;

//import static android.support.v4.content.ContextCompat.getSystemService;

public class UnitTestAction implements Action {

    private static final String CHANNEL_ID = "contextualtriggers";
    private String message;
    private Logger logger;
    private FrequentNotificationPreventionCondition notifyCondition;
    private Context ct;

    public UnitTestAction() {
        this.message = message;
        logger = Logger.getInstance();
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
