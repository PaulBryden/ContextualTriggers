package uk.ac.strath.contextualtriggers.managers;

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
import com.google.android.gms.awareness.snapshot.WeatherResponse;
import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import uk.ac.strath.contextualtriggers.MainApplication;
import uk.ac.strath.contextualtriggers.permissions.RequestLocationPermission;
import uk.ac.strath.contextualtriggers.data.WeatherData;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class WeatherDataManager extends AlarmDataManager<WeatherData> {
    int MY_PERMISSIONS_REQUEST_READ_CONTACTS;

    private final IBinder binder = new WeatherDataManager.LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        setup();
        return binder;
    }


    public class LocalBinder extends Binder {
        public IDataManager<WeatherData> getInstance() {
            return WeatherDataManager.this;
        }
    }

    public WeatherDataManager() {
        super(300, 3600);
        setup();
    }

    private void setup() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        monitor();
        alarm();
        return START_STICKY;
    }

    private void monitor() {
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainApplication.getAppActivity(),
                    ACCESS_FINE_LOCATION)) {
                Intent i = new Intent(this, RequestLocationPermission.class);
                startActivity(i);

            } else {
                ActivityCompat.requestPermissions(MainApplication.getAppActivity(),
                        new String[]{ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            }
        } else {
            Awareness.getSnapshotClient(this).getWeather().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("WeatherDataManager", e.getMessage());
                }
            }).addOnSuccessListener(new OnSuccessListener<WeatherResponse>() {
                @Override
                public void onSuccess(WeatherResponse weatherResponse) {
                    // parse and display current weather status
                    Weather weather = weatherResponse.getWeather();
                    WeatherData data = new WeatherData(weather.getTemperature(Weather.CELSIUS), weather.getHumidity(), weather.getConditions());
                    Log.d("WeatherDM", data.toString());
                    sendUpdate(data);
                }
            });
        }
    }
}

