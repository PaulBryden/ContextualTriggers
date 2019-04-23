package uk.ac.strath.contextualtriggers.managers;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;

import uk.ac.strath.contextualtriggers.Logger;
import uk.ac.strath.contextualtriggers.R;
import uk.ac.strath.contextualtriggers.data.PlacesData;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.android.volley.VolleyLog.TAG;

public class PlacesDataManager extends DataManager<PlacesData> implements IDataManager<PlacesData> {
        Logger logger;
private final IBinder binder = new PlacesDataManager.LocalBinder();
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
        return PlacesDataManager.this;
    }
}

public PlacesDataManager()
    {
        setup();
    }

    private void setup() {
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
        Intent ip = new Intent(this, PlacesDataManager.class);
        PendingIntent alarmIntent = PendingIntent.getService(this, 0, ip, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + POLLING_PERIOD, alarmIntent);
    }

    /*This Could be setup to fire on a transition, instead of a poll*/
    private void monitor()
    {
        PlacesClient placesClient;
        Places.initialize(this, getString(R.string.API_key));
        placesClient = Places.createClient(this);
        // Use fields to define the data types to return.
        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME,Place.Field.TYPES);

// Use the builder to create a FindCurrentPlaceRequest.
        FindCurrentPlaceRequest request =
                FindCurrentPlaceRequest.builder(placeFields).build();

// Call findCurrentPlace and handle the response (first check that the user has granted permission).
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
            placeResponse.addOnCompleteListener(task ->
            {
                if (task.isSuccessful())
                {
                    FindCurrentPlaceResponse response = task.getResult();
                    for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods())
                    {
                        Log.i(TAG, String.format("Place '%s' has type '%s' has likelihood: %f",
                                placeLikelihood.getPlace().getName(),
                                placeLikelihood.getPlace().getTypes(),
                                placeLikelihood.getLikelihood()));
                    }
                    sendUpdate(new PlacesData(response.getPlaceLikelihoods()));
                } else
                {
                    Exception exception = task.getException();
                    if (exception instanceof ApiException)
                    {
                        ApiException apiException = (ApiException) exception;
                        Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                    }
                }
            });
        }
    }
}
