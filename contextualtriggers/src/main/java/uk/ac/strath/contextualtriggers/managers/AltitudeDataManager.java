package uk.ac.strath.contextualtriggers.managers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.LocationResult;
import com.google.android.gms.awareness.snapshot.WeatherResult;
import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;

import uk.ac.strath.contextualtriggers.ContextualTriggersService;
import uk.ac.strath.contextualtriggers.Logger;
import uk.ac.strath.contextualtriggers.MainApplication;
import uk.ac.strath.contextualtriggers.data.AltitudeData;
import uk.ac.strath.contextualtriggers.data.WeatherData;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.android.volley.VolleyLog.TAG;

public class AltitudeDataManager extends DataManager<AltitudeData> implements IDataManager<AltitudeData> {
        Logger logger;
        AltitudeData altData;
private final IBinder binder = new AltitudeDataManager.LocalBinder();
private final int POLLING_PERIOD = 60000;

    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS;


    @Nullable
@Override
public IBinder onBind(Intent intent) {
        setup();
        return binder;
        }

public class LocalBinder extends Binder
{
    public IDataManager getInstance() {
        return AltitudeDataManager.this;
    }
}

public AltitudeDataManager()
    {
        setup();
    }

    private void setup() {
        Log.d("AltitudeDataManager","Setting Up Altitude Data Manager");
        altData = new AltitudeData();
        logger = Logger.getInstance();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        monitor();
        alarm();
        return START_STICKY;
    }

    private void alarm(){
        AlarmManager alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        Intent ip = new Intent(this, AltitudeDataManager.class);
        PendingIntent alarmIntent = PendingIntent.getService(this, 0, ip, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + POLLING_PERIOD, alarmIntent);
    }

    /*This Could be setup to fire on a transition, instead of a poll*/
    private void monitor()
    {


// Call findCurrentPlace and handle the response (first check that the user has granted permission).
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {  if (ContextCompat.checkSelfPermission(this,
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
            Awareness.SnapshotApi.getLocation(ContextualTriggersService.getGoogleAPIClient())
                    .setResultCallback(new ResultCallback<LocationResult>() {
                        @Override
                        public void onResult(@NonNull LocationResult locationResult) {
                            if (locationResult.getStatus().isSuccess()) {
                                Location location = locationResult.getLocation();
                                altData.altitude=location.getAltitude();
                                Log.d("AltitudeDataManager","Altitude:"+altData.altitude);
                                sendUpdate(altData);
                            }
                        }
                    });
        }
        }
    }
}
