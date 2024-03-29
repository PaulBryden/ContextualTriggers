package uk.ac.strath.contextualtriggers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

import uk.ac.strath.contextualtriggers.intentReceivers.ToastBroadcastReceiver;
import uk.ac.strath.contextualtriggers.managers.ActivityDataManager;
import uk.ac.strath.contextualtriggers.managers.ActualStepAndGoalDataManager;
import uk.ac.strath.contextualtriggers.managers.AltitudeDataManager;
import uk.ac.strath.contextualtriggers.managers.BatteryDataManager;
import uk.ac.strath.contextualtriggers.managers.CalendarDataManager;
import uk.ac.strath.contextualtriggers.managers.IntervalsDataManager;
import uk.ac.strath.contextualtriggers.managers.NotificationDataManager;
import uk.ac.strath.contextualtriggers.managers.PlacesDataManager;
import uk.ac.strath.contextualtriggers.managers.WeatherDataManager;
import uk.ac.strath.contextualtriggers.triggers.DefaultTriggers;
import uk.ac.strath.contextualtriggers.triggers.ITrigger;

public class ContextualTriggersService extends Service {

    private class BaseServiceConnection implements ServiceConnection {

        private ContextualTriggersService mainService;

        private IBinder dataManager;
        private boolean connected;

        public BaseServiceConnection(ContextualTriggersService mainService) {
            this.mainService = mainService;
            connected = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("BaseServiceConnection", "Connected");
            dataManager = service;
            connected = true;
            mainService.notifyDataManagerOnline();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            connected = false;
        }

        public boolean isConnected() {
            return connected;
        }

        public IBinder getDataManager() {
            return dataManager;
        }
    }

    private static GoogleApiClient mGoogleApiClient;
    private static List<ITrigger> triggerList = new ArrayList<>();

    private BaseServiceConnection weatherServiceConnection;
    private BaseServiceConnection activityServiceConnection;
    private BaseServiceConnection placesServiceConnection;
    private BaseServiceConnection actualStepsServiceConnection;
    private BaseServiceConnection calendarServiceConnection;
    private BaseServiceConnection notifyServiceConnection;
    private BaseServiceConnection batteryServiceConnection;
    private BaseServiceConnection altitudeServiceConnection;
    private BaseServiceConnection intervalServiceConnection;

    private ToastBroadcastReceiver receiverToast;

    public static GoogleApiClient getGoogleAPIClient() {
        return mGoogleApiClient;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(startId, getServiceNotification());
        //noinspection MissingPermission
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Awareness.getSnapshotClient(getApplicationContext()).getApi())
                .build();
        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                startDataManagers();
            }

            @Override
            public void onConnectionSuspended(int i) {

            }
        });
        receiverToast = new ToastBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("uk.ac.strath.contextualtriggers.toast");
        registerReceiver(receiverToast, filter);
        mGoogleApiClient.connect();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("ContextualTriggersService", "Destroying service");
        unbindService(weatherServiceConnection);
        unbindService(notifyServiceConnection);
        unbindService(placesServiceConnection);
        unbindService(activityServiceConnection);
        unbindService(actualStepsServiceConnection);
        unbindService(calendarServiceConnection);
        unbindService(batteryServiceConnection);
        unbindService(altitudeServiceConnection);
        unbindService(intervalServiceConnection);
    }


    // These probably won't be needed
    public static void addTrigger(ITrigger t) {
        triggerList.add(t);
    }

    public static void removeTrigger(ITrigger t) {
        triggerList.remove(t);
    }


    private void startDataManagers() {
        intervalServiceConnection = new BaseServiceConnection(this);
        Intent idm = new Intent(this, IntervalsDataManager.class);
        boolean b = bindService(idm, intervalServiceConnection, 0);
        startService(idm);
        altitudeServiceConnection = new BaseServiceConnection(this);
        Intent adm = new Intent(this, AltitudeDataManager.class);
        b = bindService(adm, altitudeServiceConnection, 0);
        startService(adm);
        batteryServiceConnection = new BaseServiceConnection(this); //THIS IS REQUIRED.
        Intent bdm = new Intent(this, BatteryDataManager.class);
        b = bindService(bdm, batteryServiceConnection, 0);
        startService(bdm);
        notifyServiceConnection = new BaseServiceConnection(this);
        Intent cns = new Intent(this, NotificationDataManager.class);
        b = bindService(cns, notifyServiceConnection, 0);
        startService(cns);
        calendarServiceConnection = new BaseServiceConnection(this);
        Intent cs = new Intent(this, CalendarDataManager.class);
        b = bindService(cs, calendarServiceConnection, 0);
        startService(cs);
        actualStepsServiceConnection = new BaseServiceConnection(this);
        Intent ias = new Intent(this, ActualStepAndGoalDataManager.class);
        b = bindService(ias, actualStepsServiceConnection, 0);
        startService(ias);
        placesServiceConnection = new BaseServiceConnection(this);
        Intent ip = new Intent(this, PlacesDataManager.class);
        b = bindService(ip, placesServiceConnection, 0);
        startService(ip);
        Log.d("ContextualTriggersService", "Places connection bound: " + b);
        activityServiceConnection = new BaseServiceConnection(this);
        Intent ia = new Intent(this, ActivityDataManager.class);
        b = bindService(ia, activityServiceConnection, 0);
        startService(ia);
        Log.d("ContextualTriggersService", "Activity connection bound: " + b);
        weatherServiceConnection = new BaseServiceConnection(this);
        Intent iw = new Intent(this, WeatherDataManager.class);
        b = bindService(iw, weatherServiceConnection, 0);
        startService(iw);
        Log.d("ContextualTriggersService", "Weather connection bound: " + b);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void notifyDataManagerOnline() {
        if (weatherServiceConnection.isConnected() && notifyServiceConnection.isConnected() &&
                placesServiceConnection.isConnected() && activityServiceConnection.isConnected() &&
                actualStepsServiceConnection.isConnected() && calendarServiceConnection.isConnected() &&
                batteryServiceConnection.isConnected() && altitudeServiceConnection.isConnected() &&
                intervalServiceConnection.isConnected()) {
            Log.d("ContextualTriggersService", "Data manager online");
            createTriggers();
        } else {
            Log.d("ContextualTriggersService", "Data manager not online");
        }
    }

    private void createTriggers() {
        Log.d("ContextualTriggersService", "Creating triggers");
        triggerList.add(DefaultTriggers.timeToWalk(activityServiceConnection.getDataManager(), intervalServiceConnection.getDataManager(), notifyServiceConnection.getDataManager()));
        triggerList.add(DefaultTriggers.gyminyCricket(placesServiceConnection.getDataManager(), notifyServiceConnection.getDataManager()));
        triggerList.add(DefaultTriggers.halfAndHalf(actualStepsServiceConnection.getDataManager(), intervalServiceConnection.getDataManager(), notifyServiceConnection.getDataManager()));
        triggerList.add(DefaultTriggers.butItsSunnyOutside(actualStepsServiceConnection.getDataManager(), weatherServiceConnection.getDataManager(), activityServiceConnection.getDataManager(), notifyServiceConnection.getDataManager()));
        triggerList.add(DefaultTriggers.goingDown(actualStepsServiceConnection.getDataManager(), placesServiceConnection.getDataManager(), altitudeServiceConnection.getDataManager(), notifyServiceConnection.getDataManager()));
        triggerList.add(DefaultTriggers.walkAndTalk(actualStepsServiceConnection.getDataManager(), calendarServiceConnection.getDataManager(), notifyServiceConnection.getDataManager()));
        triggerList.add(DefaultTriggers.danceForYourDinner(actualStepsServiceConnection.getDataManager(), placesServiceConnection.getDataManager(), notifyServiceConnection.getDataManager()));
        triggerList.add(DefaultTriggers.walkToWorkOnWeekdays(actualStepsServiceConnection.getDataManager(), intervalServiceConnection.getDataManager(), notifyServiceConnection.getDataManager()));
        triggerList.add(DefaultTriggers.congratulations(placesServiceConnection.getDataManager(), notifyServiceConnection.getDataManager()));
        Log.i("ContextualTriggersService", "Created " + triggerList.size() + " triggers");

        unbindService(weatherServiceConnection);
        unbindService(notifyServiceConnection);
        unbindService(placesServiceConnection);
        unbindService(activityServiceConnection);
        unbindService(actualStepsServiceConnection);
        unbindService(calendarServiceConnection);
        unbindService(batteryServiceConnection);
        unbindService(altitudeServiceConnection);
        unbindService(intervalServiceConnection);
    }

    private Notification getServiceNotification() {
        createNotificationChannel();
        Intent pIntent = new Intent(this, ToastBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, pIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainApplication.getAppContext(), "cts")
                .setSmallIcon(R.drawable.round_directions_walk_24)
                .setContentTitle("Contextual Triggers Framework")
                .setContentText("Contextual Triggers Service Running")
                .setContentIntent(pendingIntent);
        return builder.build();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "contextualtriggers";
            String description = "contextualtriggers channel";
            NotificationChannel channel = new NotificationChannel("cts", name, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = (NotificationManager) MainApplication.getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }


}
