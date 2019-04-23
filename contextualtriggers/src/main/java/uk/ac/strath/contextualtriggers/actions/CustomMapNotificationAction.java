package uk.ac.strath.contextualtriggers.actions;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.LocationResponse;
import com.google.android.gms.awareness.snapshot.LocationResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.OnSuccessListener;

import uk.ac.strath.contextualtriggers.ContextualTriggersService;
import uk.ac.strath.contextualtriggers.MainApplication;
import uk.ac.strath.contextualtriggers.R;
import uk.ac.strath.contextualtriggers.conditions.FrequentNotificationPreventionCondition;
import uk.ac.strath.contextualtriggers.managers.NotificationDataManager;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class CustomMapNotificationAction implements Action {

    private static final String CHANNEL_ID = "contextualtriggers";
    private String message;
    private String queryLocation;
    private FrequentNotificationPreventionCondition notifyCondition;
    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS;

    public CustomMapNotificationAction(String message, String queryLocation) {
        this.message = message;
        this.queryLocation = queryLocation;
        createNotificationChannel();
    }

    private void executeMapsNotification() {
        if (ContextCompat.checkSelfPermission(MainApplication.getAppContext(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(MainApplication.getAppContext(),
                    ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainApplication.getAppActivity(),
                        ACCESS_FINE_LOCATION)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(MainApplication.getAppActivity(),
                            new String[]{ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                }
            } else {
                // Permission has already been granted
                Awareness.getSnapshotClient(MainApplication.getAppContext()).getLocation().addOnSuccessListener(new OnSuccessListener<LocationResponse>() {
                    @Override
                    public void onSuccess(LocationResponse locationResponse) {
                        Location localLocation = locationResponse.getLocation();
                        Intent cs = new Intent(MainApplication.getAppContext(), NotificationDataManager.class);
                        MainApplication.getAppContext().startService(cs);
                        if (notifyCondition != null) {
                            notifyCondition.notifyUpdate(null);
                        }
                        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + queryLocation + "&mode=w");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        PendingIntent pIntent = PendingIntent.getActivity(MainApplication.getAppContext(), 0, mapIntent, 0);
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainApplication.getAppContext(), CHANNEL_ID)
                                .setSmallIcon(R.drawable.round_directions_walk_24)
                                .setContentTitle("Notification")
                                .setContentText(message)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setContentIntent(pIntent);
                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainApplication.getAppContext());
                        // notificationId is a unique int for each notification that you must define
                        notificationManager.notify(0, builder.build());
                    }
                });
            }
        }
    }

    @Override
    public void execute() {
        executeMapsNotification();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "contextualtriggers";
            String description = "contextualtriggers channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = (NotificationManager) MainApplication.getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void attachCondition(FrequentNotificationPreventionCondition notifyCondition) {
        this.notifyCondition = notifyCondition;
    }

}
