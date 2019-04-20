package uk.ac.strath.contextualtriggers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
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

import uk.ac.strath.contextualtriggers.managers.ActivityDataManager;
import uk.ac.strath.contextualtriggers.managers.ActualGoalDataManager;
import uk.ac.strath.contextualtriggers.managers.ActualStepAndGoalDataManager;
import uk.ac.strath.contextualtriggers.managers.AltitudeDataManager;
import uk.ac.strath.contextualtriggers.managers.BatteryDataManager;
import uk.ac.strath.contextualtriggers.managers.CalendarDataManager;
import uk.ac.strath.contextualtriggers.managers.NotificationDataManager;
import uk.ac.strath.contextualtriggers.managers.PlacesDataManager;
import uk.ac.strath.contextualtriggers.managers.SimulatedStepDataManager;
import uk.ac.strath.contextualtriggers.managers.WeatherDataManager;
import uk.ac.strath.contextualtriggers.services.AbstractServiceConnection;
import uk.ac.strath.contextualtriggers.triggers.DefaultTriggers;
import uk.ac.strath.contextualtriggers.triggers.ITrigger;

public class ContextualTriggersService extends Service {

    private static GoogleApiClient mGoogleApiClient;
    private static List<ITrigger> triggerList = new ArrayList<>();
    private static List<IBinder> serviceList = new ArrayList<>();

    private AbstractServiceConnection stepServiceConnection;
    private AbstractServiceConnection weatherServiceConnection;
    private AbstractServiceConnection activityServiceConnection;
    private AbstractServiceConnection placesServiceConnection;
    private AbstractServiceConnection actualStepsServiceConnection;
    private AbstractServiceConnection calendarServiceConnection;
    private AbstractServiceConnection notifyServiceConnection;
    private AbstractServiceConnection batteryServiceConnection;
    private AbstractServiceConnection altitudeServiceConnection;

    public static GoogleApiClient getGoogleAPIClient() {
        return mGoogleApiClient;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(startId, getServiceNotification());
        //noinspection MissingPermission
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Awareness.API)
                .build();
        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle)
            {
                    startDataManagers();
            }

            @Override
            public void onConnectionSuspended(int i) {

            }
        });
        mGoogleApiClient.connect();
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("EXIT", "ondestroy!");
        //Intent broadcastIntent = new Intent(this, ContextualTriggersService.class);
        unbindService(weatherServiceConnection);
        unbindService(stepServiceConnection);
        unbindService(placesServiceConnection);
        unbindService(activityServiceConnection);
    }


    // These probably won't be needed
    public static void addTrigger(ITrigger t) {
        triggerList.add(t);
    }

    public static void removeTrigger(ITrigger t) {
        triggerList.remove(t);
    }


    private void startDataManagers() {
        altitudeServiceConnection= new AbstractServiceConnection(this);
        Intent adm = new Intent(this, AltitudeDataManager.class);
        boolean b = bindService(adm, altitudeServiceConnection, 0);
        startService(adm);
        batteryServiceConnection = new AbstractServiceConnection(this); //THIS IS REQUIRED.
        Intent bdm = new Intent(this, BatteryDataManager.class);
        b = bindService(bdm, batteryServiceConnection, 0);
        startService(bdm);
        notifyServiceConnection = new AbstractServiceConnection(this);
        Intent cns = new Intent(this, NotificationDataManager.class);
        b = bindService(cns, notifyServiceConnection, 0);
        startService(cns);
        calendarServiceConnection = new AbstractServiceConnection(this);
        Intent cs = new Intent(this, CalendarDataManager.class);
        b = bindService(cs, calendarServiceConnection, 0);
        startService(cs);

        actualStepsServiceConnection = new AbstractServiceConnection(this);
        Intent ias = new Intent(this, ActualStepAndGoalDataManager.class);
         b = bindService(ias, actualStepsServiceConnection, 0);
        startService(ias);
      //  pointlessTrigger();
        placesServiceConnection = new AbstractServiceConnection(this);
        Intent ip = new Intent(this, PlacesDataManager.class);
        b = bindService(ip, placesServiceConnection, 0);
        startService(ip);
        Log.d("BindingActervice", Boolean.toString(b));
        activityServiceConnection = new AbstractServiceConnection(this);
        Intent ia = new Intent(this, ActivityDataManager.class);
        b = bindService(ia, activityServiceConnection, 0);
        startService(ia);
        Log.d("BindingActervice", Boolean.toString(b));
        weatherServiceConnection = new AbstractServiceConnection(this);
            Intent iw = new Intent(this, WeatherDataManager.class);
            b = bindService(iw, weatherServiceConnection, 0);
            startService(iw);
            Log.d("BindingWeatherService", Boolean.toString(b));
            stepServiceConnection = new AbstractServiceConnection(this);
            Intent is = new Intent(this, SimulatedStepDataManager.class);
        b = bindService(is, stepServiceConnection, 0);
        startService(is);
            Log.d("BindingStepService", Boolean.toString(b));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void notifyDataManagerOnline() {
        if (stepServiceConnection.isConnected() && weatherServiceConnection.isConnected()&& notifyServiceConnection.isConnected()) {
            Log.d("DataManagerOnline", "Data Managers online");
            createTriggers();
        } else {
            Log.d("DataManagerOnline", ": " + stepServiceConnection.isConnected());
        }
    }

    private void createTriggers() {
        Log.d("Creating Triggers", "created");
       // triggerList.add(DefaultTriggers.createWeatherTrigger(stepServiceConnection.getDataManager(), weatherServiceConnection.getDataManager(), this));
       // triggerList.add(DefaultTriggers.createWalkIdleTrigger(stepServiceConnection.getDataManager(), this));
        triggerList.add(DefaultTriggers.createWeatherWithNotifyLimitTrigger(stepServiceConnection.getDataManager(), weatherServiceConnection.getDataManager(),notifyServiceConnection.getDataManager(), this));

        unbindService(weatherServiceConnection);
        unbindService(stepServiceConnection);
        unbindService(notifyServiceConnection);
        unbindService(placesServiceConnection);
        unbindService(activityServiceConnection);
        unbindService(actualStepsServiceConnection);
        unbindService(calendarServiceConnection);

    }

    private Notification getServiceNotification(){
        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainApplication.getAppContext(), "cts")
                .setSmallIcon(R.drawable.powered_by_google_dark)
                .setContentTitle("ContextualTriggerService")
                .setContentText("Context Service Running");
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
