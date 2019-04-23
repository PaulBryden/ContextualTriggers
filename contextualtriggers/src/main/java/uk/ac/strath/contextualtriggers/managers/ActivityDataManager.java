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
import com.google.android.gms.awareness.snapshot.DetectedActivityResult;
import com.google.android.gms.awareness.snapshot.WeatherResult;
import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import uk.ac.strath.contextualtriggers.ContextualTriggersService;
import uk.ac.strath.contextualtriggers.Logger;
import uk.ac.strath.contextualtriggers.MainApplication;
import uk.ac.strath.contextualtriggers.data.ActivityData;
import uk.ac.strath.contextualtriggers.data.WeatherData;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
//import static com.google.android.gms.internal.zzs.TAG;

public class ActivityDataManager extends DataManager<ActivityData> implements IDataManager<ActivityData> {
    Logger logger;
    private final IBinder binder = new ActivityDataManager.LocalBinder();
    private final int POLLING_PERIOD = 5000;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        setup();
        return binder;
    }

    public class LocalBinder extends Binder {
        public IDataManager getInstance() {
            return ActivityDataManager.this;
        }
    }

    public ActivityDataManager()
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
            Intent ia = new Intent(this, ActivityDataManager.class);
            PendingIntent alarmIntent = PendingIntent.getService(this, 0, ia, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + POLLING_PERIOD,
                    alarmIntent);
        }


        /*This Could be setup to fire on a transition, instead of a poll*/
        private void monitor() {
                // Permission has already been granted
                Awareness.SnapshotApi.getDetectedActivity(ContextualTriggersService.getGoogleAPIClient())
                        .setResultCallback(new ResultCallback<DetectedActivityResult>() {
                            @Override
                            public void onResult(@NonNull DetectedActivityResult detectedActivityResult) {
                                if (!detectedActivityResult.getStatus().isSuccess()) {
                                    Log.d("ActivityDataManager", "Could not get the current activity.");
                                    return;
                                }
                                 ActivityRecognitionResult ar = detectedActivityResult.getActivityRecognitionResult();
                                DetectedActivity probableActivity = ar.getMostProbableActivity();
                                Log.d("ActivityDataManager", probableActivity.toString());
                                sendUpdate(new ActivityData(probableActivity));
                            }
                        });
            }

    }

