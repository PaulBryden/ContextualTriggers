package uk.ac.strath.contextualtriggers;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

import uk.ac.strath.contextualtriggers.data.WeatherData;
import uk.ac.strath.contextualtriggers.managers.StepDataManager;
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
    private AlarmManager alarmMgr;

    public static GoogleApiClient getGoogleAPIClient() {
        return mGoogleApiClient;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
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
    }
    // These probably won't be needed
    public static void addTrigger(ITrigger t) {
        triggerList.add(t);
    }

    public static void removeTrigger(ITrigger t) {
        triggerList.remove(t);
    }


    private void startDataManagers() {
      //  pointlessTrigger();
        alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);

            weatherServiceConnection = new AbstractServiceConnection(this);
            Intent iw = new Intent(this, WeatherDataManager.class);
            startService(iw);
            boolean b = bindService(iw, weatherServiceConnection, Context.BIND_AUTO_CREATE);
            Log.d("BindingWeatherService", Boolean.toString(b));
            Log.d("TriggerManager", "Creating default triggers");
            PendingIntent alarmIntent;
            alarmIntent = PendingIntent.getService(this, 0, iw, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
                    SystemClock.elapsedRealtime() + 5000,
                    5000, alarmIntent);
            stepServiceConnection = new AbstractServiceConnection(this);
            Intent is = new Intent(this, StepDataManager.class);
        startService(is);
             b = bindService(is, stepServiceConnection, Context.BIND_AUTO_CREATE);
            Log.d("BindingStepService", Boolean.toString(b));
            PendingIntent alarmIntent2;
            alarmIntent2 = PendingIntent.getService(this, 0, is, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
                    SystemClock.elapsedRealtime() + 5000,
                    5000, alarmIntent2);


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void notifyDataManagerOnline() {
        if (stepServiceConnection.isConnected() && weatherServiceConnection.isConnected()) {
            Log.d("DataManagerOnline", "Data Managers online");
            createTriggers();
        } else {
            Log.d("DataManagerOnline", ": " + stepServiceConnection.isConnected());
        }
    }

    private void createTriggers() {
        triggerList.add(DefaultTriggers.createWeatherTrigger(stepServiceConnection.getDataManager(), weatherServiceConnection.getDataManager()));
        triggerList.add(DefaultTriggers.createWalkIdleTrigger(stepServiceConnection.getDataManager()));
    }
}
