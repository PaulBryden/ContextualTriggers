package uk.ac.strath.contextualtriggers.managers;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.DetectedActivityResult;
import com.google.android.gms.awareness.snapshot.PlacesResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.places.PlaceLikelihood;

import java.util.List;

import uk.ac.strath.contextualtriggers.ContextualTriggersService;
import uk.ac.strath.contextualtriggers.Logger;
import uk.ac.strath.contextualtriggers.MainApplication;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.app.Service.START_STICKY;

public class PlacesDataManager extends DataManager<DetectedActivity> implements IDataManager<DetectedActivity> {
        Logger logger;
private final IBinder binder = new PlacesDataManager.LocalBinder();
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
        return PlacesDataManager.this;
    }
}
    PlacesDataManager()
    {
        setup();
    }

    private void setup() {
        logger = Logger.getInstance();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStart(intent, startId);
        monitor();
        return START_STICKY;
    }


    /*This Could be setup to fire on a transition, instead of a poll*/
    private void monitor() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainApplication.getAppActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
Awareness.SnapshotApi.getPlaces(ContextualTriggersService.getGoogleAPIClient())
        .setResultCallback(new ResultCallback<PlacesResult>() {
            @Override
            public void onResult(@NonNull PlacesResult placesResult) {
                if (!placesResult.getStatus().isSuccess()) {
                    Log.d("PlacesDataManager", "Could not get places."+placesResult.getStatus().getStatusCode());
                    return;
                }
                List<PlaceLikelihood> placeLikelihoodList = placesResult.getPlaceLikelihoods();
                // Show the top 5 possible location results.
                for (int i = 0; i < 5; i++) {
                    PlaceLikelihood p = placeLikelihoodList.get(i);
                    Log.d("PlacesDataManager", p.getPlace().getName().toString() + ", likelihood: " + p.getLikelihood());
                }
            }
        });
    }
    }
}
