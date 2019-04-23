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
import uk.ac.strath.contextualtriggers.RequestLocationPermission;
import uk.ac.strath.contextualtriggers.data.WeatherData;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class WeatherDataManager extends DataManager<WeatherData> implements IDataManager<WeatherData> {
    int MY_PERMISSIONS_REQUEST_READ_CONTACTS;
    Logger logger;

    private final IBinder binder = new WeatherDataManager.LocalBinder();

    private final int POLLING_PERIOD = 30000;
    private AlarmManager alarmMgr ;



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        setup();
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        Log.d("WEATHER", "HAS BEEN OBLITERATED");
        Intent iw = new Intent(this, WeatherDataManager.class);
        PendingIntent alarmIntent = PendingIntent.getService(this, 0, iw, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmMgr.cancel(alarmIntent);
    }

    public class LocalBinder extends Binder {
        public IDataManager<WeatherData> getInstance() {
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
            alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
            Intent iw = new Intent(this, WeatherDataManager.class);
            PendingIntent alarmIntent = PendingIntent.getService(this, 0, iw, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + POLLING_PERIOD, alarmIntent);
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

