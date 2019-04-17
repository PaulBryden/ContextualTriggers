package uk.ac.strath.contextualtriggers;

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

import uk.ac.strath.contextualtriggers.managers.DataManager;
import uk.ac.strath.contextualtriggers.managers.IDataManager;
import uk.ac.strath.contextualtriggers.managers.StepDataManager;
//import uk.ac.strath.contextualtriggers.triggers.ButItsSunnyOutsideTrigger;
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
            public void onConnected(@Nullable Bundle bundle) {

                startDataManagers();
            }

            @Override
            public void onConnectionSuspended(int i) {

            }
        });
        mGoogleApiClient.connect();
        return START_STICKY;
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
        weatherServiceConnection = new AbstractServiceConnection(this);
        stepServiceConnection = new AbstractServiceConnection(this);
        Intent is = new Intent(this, StepDataManager.class);
        startService(is);
        boolean b = bindService(is, stepServiceConnection, Context.BIND_AUTO_CREATE);
        Log.d("BindingStepService", Boolean.toString(b));
        Log.d("TriggerManager", "Creating default triggers");
        Intent iw = new Intent(this, WeatherDataManager.class);
        startService(iw);
        b = bindService(iw, weatherServiceConnection, Context.BIND_AUTO_CREATE);
        Log.d("BindingWeatherService", Boolean.toString(b));
        PendingIntent alarmIntent;
        PendingIntent alarmIntent2;
        alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        alarmIntent = PendingIntent.getService(this, 0, is, 0);
        alarmMgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 10000,
                10000, alarmIntent);
        alarmIntent2 = PendingIntent.getService(this, 0, iw, 0);
        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 10000,
                10000, alarmIntent2);

    }

    private void addService(IBinder b){
        serviceList.add(b);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void pointlessTrigger(){
        //Pointless Trigger creation
        Log.d("TriggerManager", "Creating default triggers");
        Intent i = new Intent(this, StepDataManager.class);
        boolean b = bindService(i, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d("TriggerManager", "Connected to StepDataManager");  try {
                    triggerList.add(DefaultTriggers.createStepMonitorTrigger(service));
                }catch(ClassCastException e){
                    Log.e("Trigger Creation", "Failed building trigger: " + e.getMessage());
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, Context.BIND_AUTO_CREATE);
        startService(i);
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
