package uk.ac.strath.contextualtriggers.managers;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.LocationResult;
import com.google.android.gms.common.api.ResultCallback;

import uk.ac.strath.contextualtriggers.ContextualTriggersService;
import uk.ac.strath.contextualtriggers.Logger;
import uk.ac.strath.contextualtriggers.MainApplication;
import uk.ac.strath.contextualtriggers.RequestLocationPermission;
import uk.ac.strath.contextualtriggers.data.AltitudeData;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class AltitudeDataManager extends AlarmDataManager<AltitudeData> {
    Logger logger;
    AltitudeData altData;
    private final IBinder binder = new AltitudeDataManager.LocalBinder();

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
        super(60, 240);
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

    /*This Could be setup to fire on a transition, instead of a poll*/
    private void monitor()
    {
        Log.d("ALT", "Before check perm");


// Call findCurrentPlace and handle the response (first check that the user has granted permission).
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {   // Permission is not granted
            Log.d("ALT", "After check perm");
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainApplication.getAppActivity(),
                    ACCESS_FINE_LOCATION)) {
                Log.d("ALT", "Show rationale check perm");
                Intent i = new Intent(this, RequestLocationPermission.class);
                startActivity(i);

            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MainApplication.getAppActivity(),
                        new String[]{ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                Log.d("ALT", "Request check perm");

            }
        } else {
            // try {
            // Permission has already been granted
            Log.d("ALT", "Accepted check perm");
            Awareness.SnapshotApi.getLocation(ContextualTriggersService.getGoogleAPIClient())
                    .setResultCallback(new ResultCallback<LocationResult>() {
                        @Override
                        public void onResult(@NonNull LocationResult locationResult) {
                            if (locationResult.getStatus().isSuccess()) {
                                Location location = locationResult.getLocation();
                                altData.altitude = location.getAltitude();
                                Log.d("AltitudeDataManager", "Altitude:" + altData.altitude);
                                sendUpdate(altData);
                            } else {
                                Log.d("altitudeDataManager", "Failed " + locationResult.getStatus().toString());
                            }
                        }
                    });
        }
    }
}
