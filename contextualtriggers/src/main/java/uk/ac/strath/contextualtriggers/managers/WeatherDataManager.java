package uk.ac.strath.contextualtriggers.managers;

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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.WeatherResult;
import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.common.api.ResultCallback;

import uk.ac.strath.contextualtriggers.ContextualTriggersService;
import uk.ac.strath.contextualtriggers.Logger;
import uk.ac.strath.contextualtriggers.MainApplication;
import uk.ac.strath.contextualtriggers.data.WeatherData;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class WeatherDataManager extends DataManager<WeatherData> implements IDataManager<WeatherData> {
    int MY_PERMISSIONS_REQUEST_READ_CONTACTS;
    Logger logger;

    private final IBinder binder = new WeatherDataManager.LocalBinder();



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        setup();
        return binder;
    }

    public class LocalBinder extends Binder {
        public IDataManager getInstance() {
            return WeatherDataManager.this;
        }
    }
    public WeatherDataManager()
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


        private void alarm() {
            AlarmManager alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
            Intent iw = new Intent(this, WeatherDataManager.class);
            PendingIntent alarmIntent = PendingIntent.getService(this, 0, iw, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + 30000, alarmIntent);
        }

        private void monitor() {
            if (ContextCompat.checkSelfPermission(this,
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
                Awareness.SnapshotApi.getWeather(ContextualTriggersService.getGoogleAPIClient())
                        .setResultCallback(new ResultCallback<WeatherResult>() {
                            @Override
                            public void onResult(@NonNull WeatherResult weatherResult) {
                                if (!weatherResult.getStatus().isSuccess()) {
                                    Log.d("WeatherDM", weatherResult.getStatus().toString());
                                    Log.e("WeatherDataManager", weatherResult.getStatus().getStatusMessage()+" ");
                                    return;
                                }

                                //parse and display current weather status
                                Weather weather = weatherResult.getWeather();
                                WeatherData data = new WeatherData();
                                data.lastUpdateTime = System.currentTimeMillis();
                                data.TemperatureCelsius = weather.getTemperature(Weather.CELSIUS);
                                data.Humidity = weather.getHumidity();
                                data.Conditions = weather.getConditions();
                                Log.d("WeatherDM", data.toString());
                                data.printData();
                                logger.log(data.toString());
                                sendUpdate(data);
                            }
                        });
            }
        }
    }

