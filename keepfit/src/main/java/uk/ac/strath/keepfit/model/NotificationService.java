package uk.ac.strath.keepfit.model;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import uk.ac.strath.keepfit.R;
import uk.ac.strath.keepfit.view.MainActivity;

public class NotificationService extends Service {

    private final static String NOTIFICATION_CHANNEL_ID = "keepFitNotificationChannel";
    private static final int HALFWAY_NOTIFICATION_TIMEOUT = 60*60*12;
    private static int idCounter = 0;
    private static long lastHalfwayNotificationSent = 0;
    private static int lastHalfwayNotificationId = -1;
    private SharedPreferencesManager spm;

    public static synchronized int createId() {
        return idCounter++;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        spm = new SharedPreferencesManager(this);
        createNotificationChannel();
        sendHalfwayNotification();
        return START_STICKY;
    }

    private Notification buildNotification(int titleRes, int contentTextRes, PendingIntent contentIntent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        builder.setContentTitle(getString(titleRes))
                .setContentText(getString(contentTextRes))
                .setSmallIcon(R.drawable.ic_keepfit_black_24dp)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(contentIntent)
                .setAutoCancel(true);
        return builder.build();
    }

    private void sendHalfwayNotification() {
        if (spm.areNotificationsTurnedOn() && timeSince(lastHalfwayNotificationSent) > HALFWAY_NOTIFICATION_TIMEOUT) {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            Intent intent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            Notification notification = buildNotification(R.string.notification_title_halfway, R.string.notification_content_halfway, pendingIntent);
            notificationManager.cancel(lastHalfwayNotificationId);
            lastHalfwayNotificationId = createId();
            notificationManager.notify(lastHalfwayNotificationId, notification);
            lastHalfwayNotificationSent = System.currentTimeMillis();
        }
    }

    private static int timeSince(long start) {
        if (start == 0) {
            return Integer.MAX_VALUE;
        }
        return (int) ((System.currentTimeMillis() - start) / 1000);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel name";
            String description = "channel description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
