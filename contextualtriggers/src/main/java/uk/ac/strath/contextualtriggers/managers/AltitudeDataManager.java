package uk.ac.strath.contextualtriggers.managers;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.LocationResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import uk.ac.strath.contextualtriggers.data.AltitudeData;
import uk.ac.strath.contextualtriggers.permissions.RequestLocationPermission;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class AltitudeDataManager extends AlarmDataManager<AltitudeData> {
    AltitudeData altData;
    private final IBinder binder = new AltitudeDataManager.LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        setup();
        return binder;
    }

    public class LocalBinder extends Binder {
        public IDataManager<AltitudeData> getInstance() {
            return AltitudeDataManager.this;
        }
    }

    public AltitudeDataManager() {
        super(60, 240);
        setup();
    }

    private void setup() {
        Log.d("AltitudeDataManager", "Setting Up Altitude Data Manager");
        altData = null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        monitor();
        alarm();
        return START_STICKY;
    }

    /*This Could be setup to fire on a transition, instead of a poll*/
    private void monitor() {
        Log.d("AltitudeDataManager", "Checking permsissions");

        // Call findCurrentPlace and handle the response (first check that the user has granted permission).
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {   // Permission is not granted
            Intent i = new Intent(this, RequestLocationPermission.class);
            startActivity(i);
        } else {
            // try {
            // Permission has already been granted
            Log.d("AltitudeDataManager", "Accepted check perm");
            Awareness.getSnapshotClient(this).getLocation().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w("AltitudeDataManager", e.getMessage(), e);
                }
            }).addOnSuccessListener(new OnSuccessListener<LocationResponse>() {
                @Override
                public void onSuccess(LocationResponse locationResponse) {
                    Location location = locationResponse.getLocation();
                    altData = new AltitudeData(location.getAltitude());
                    Log.d("AltitudeDataManager", "Altitude: " + altData.getAltitude());
                    sendUpdate(altData);
                }
            });
        }
    }
}
