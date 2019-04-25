package uk.ac.strath.contextualtriggers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

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
import uk.ac.strath.contextualtriggers.managers.SimulatedStepDataManager;
import uk.ac.strath.contextualtriggers.managers.WeatherDataManager;
import uk.ac.strath.contextualtriggers.triggers.DefaultTriggers;
import uk.ac.strath.contextualtriggers.triggers.ITrigger;

public class ContextualTriggersService extends Service
{

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
            Log.d("AbstractService", "Are we here?");
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

        public IBinder getDataManager(){
            return dataManager;
        }
    }

    private static GoogleApiClient mGoogleApiClient;
    private static List<ITrigger> triggerList = new ArrayList<>();
    private static List<IBinder> serviceList = new ArrayList<>();

    private BaseServiceConnection stepServiceConnection;
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
    
    public static GoogleApiClient getGoogleAPIClient()
    {
        return mGoogleApiClient;
    }

    public int onStartCommand(Intent intent, int flags, int startId)
    {
        startForeground(startId, getServiceNotification());
        //noinspection MissingPermission
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Awareness.getSnapshotClient(getApplicationContext()).getApi())
                .build();
        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks()
        {
            @Override
            public void onConnected(@Nullable Bundle bundle)
            {
                startDataManagers();
            }

            @Override
            public void onConnectionSuspended(int i)
            {

            }
        });
        receiverToast = new ToastBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("uk.ac.strath.contextualtriggers.toast");
        registerReceiver(receiverToast,filter);
        mGoogleApiClient.connect();
        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.i("EXIT", "ondestroy!");
        //Intent broadcastIntent = new Intent(this, ContextualTriggersService.class);
        unbindService(weatherServiceConnection);
        unbindService(stepServiceConnection);
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
    public static void addTrigger(ITrigger t)
    {
        triggerList.add(t);
    }

    public static void removeTrigger(ITrigger t)
    {
        triggerList.remove(t);
    }


    private void startDataManagers()
    {
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
        Log.d("BindingActervice", Boolean.toString(b));
        activityServiceConnection = new BaseServiceConnection(this);
        Intent ia = new Intent(this, ActivityDataManager.class);
        b = bindService(ia, activityServiceConnection, 0);
        startService(ia);
        Log.d("BindingActervice", Boolean.toString(b));
        weatherServiceConnection = new BaseServiceConnection(this);
        Intent iw = new Intent(this, WeatherDataManager.class);
        b = bindService(iw, weatherServiceConnection, 0);
        startService(iw);
        Log.d("BindingWeatherService", Boolean.toString(b));
        stepServiceConnection = new BaseServiceConnection(this);
        Intent is = new Intent(this, SimulatedStepDataManager.class);
        b = bindService(is, stepServiceConnection, 0);
        startService(is);
        Log.d("BindingStepService", Boolean.toString(b));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    public void notifyDataManagerOnline()
    {
        if (stepServiceConnection.isConnected() && weatherServiceConnection.isConnected() && notifyServiceConnection.isConnected())
        {
            Log.d("DataManagerOnline", "Data Managers online");
            createTriggers();
        } else
        {
            Log.d("DataManagerOnline", ": " + stepServiceConnection.isConnected());
        }
    }

    private void createTriggers()
    {
        Log.d("Creating Triggers", "created");
        //   triggerList.add(DefaultTriggers.createWeatherWithNotifyLimitTriggerReal(actualStepsServiceConnection.getDataManager(), weatherServiceConnection.getDataManager(), notifyServiceConnection.getDataManager(), this));
        triggerList.add(DefaultTriggers.TimeToWalk(activityServiceConnection.getDataManager(),intervalServiceConnection.getDataManager(),notifyServiceConnection.getDataManager()));
       // triggerList.add(DefaultTriggers.HalfAndHalf(actualStepsServiceConnection.getDataManager(),intervalServiceConnection.getDataManager(),notifyServiceConnection.getDataManager()));
        //triggerList.add(DefaultTriggers.GyminyCricket(placesServiceConnection.getDataManager(),notifyServiceConnection.getDataManager()));
        //triggerList.add(DefaultTriggers.ButItsSunnyOutside(actualStepsServiceConnection.getDataManager(),weatherServiceConnection.getDataManager(),activityServiceConnection.getDataManager(),notifyServiceConnection.getDataManager()));
        //triggerList.add(DefaultTriggers.GoingDown(actualStepsServiceConnection.getDataManager(),placesServiceConnection.getDataManager(),altitudeServiceConnection.getDataManager(),notifyServiceConnection.getDataManager()));
        //triggerList.add(DefaultTriggers.DanceForYourDinner(actualStepsServiceConnection.getDataManager(),placesServiceConnection.getDataManager(),notifyServiceConnection.getDataManager()));
        //triggerList.add(DefaultTriggers.WalkToWorkOnWeekdays(actualStepsServiceConnection.getDataManager(),intervalServiceConnection.getDataManager(),notifyServiceConnection.getDataManager()));
        //triggerList.add(DefaultTriggers.Congratulations(placesServiceConnection.getDataManager(),notifyServiceConnection.getDataManager()));

      //  triggerList.add(DefaultTriggers.WalkAndTalk(actualStepsServiceConnection.getDataManager(),calendarServiceConnection.getDataManager(),notifyServiceConnection.getDataManager()));
        unbindService(weatherServiceConnection);
        unbindService(stepServiceConnection);
        unbindService(notifyServiceConnection);
        unbindService(placesServiceConnection);
        unbindService(activityServiceConnection);
        unbindService(actualStepsServiceConnection);
        unbindService(calendarServiceConnection);
        unbindService(batteryServiceConnection);
        unbindService(altitudeServiceConnection);
        unbindService(intervalServiceConnection);
    }

    private Notification getServiceNotification()
    {
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

    private void createNotificationChannel()
    {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
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
