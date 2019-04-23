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
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.TimeIntervalsResponse;
import com.google.android.gms.awareness.snapshot.TimeIntervalsResult;
import com.google.android.gms.awareness.snapshot.WeatherResult;
import com.google.android.gms.awareness.state.TimeIntervals;
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
import uk.ac.strath.contextualtriggers.data.TimeOfDayData;
import uk.ac.strath.contextualtriggers.data.WeatherData;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.android.volley.VolleyLog.TAG;

public class IntervalsDataManager extends DataManager<TimeOfDayData> implements IDataManager<TimeOfDayData> {
        Logger logger;
private final IBinder binder = new IntervalsDataManager.LocalBinder();


    @Nullable
@Override
public IBinder onBind(Intent intent) {
        setup();
        return binder;
        }

public class LocalBinder extends Binder
{
    public IDataManager getInstance() {
        return IntervalsDataManager.this;
    }
}

public IntervalsDataManager()
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
        Intent ip = new Intent(this, IntervalsDataManager.class);
        PendingIntent alarmIntent = PendingIntent.getService(this, 0, ip, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 60000, alarmIntent);
    }

    /*This Could be setup to fire on a transition, instead of a poll*/
    private void monitor()
    {


// Call findCurrentPlace and handle the response (first check that the user has granted permission).
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            Awareness.SnapshotApi.getTimeIntervals(ContextualTriggersService.getGoogleAPIClient())
                    .setResultCallback(new ResultCallback<TimeIntervalsResult>() {
                        @Override
                        public void onResult(@NonNull TimeIntervalsResult intervalResult) {
                            if (!intervalResult.getStatus().isSuccess()) {
                                Log.d("IntervalsDM", intervalResult.getStatus().toString());
                                Log.e("IntervalsDataManager", intervalResult.getStatus().getStatusMessage()+" ");
                                return;
                            }

                            //parse and display current weather status
                            TimeIntervals intervals = intervalResult.getTimeIntervals();
                            Log.d("IntervalsDM", intervals.toString());
                            sendUpdate(new TimeOfDayData(intervals.getTimeIntervals()));
                        }
                    });
        }
    }
}
