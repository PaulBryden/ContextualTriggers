package uk.ac.strath.contextualtriggers.managers;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.TimeIntervalsResult;
import com.google.android.gms.awareness.state.TimeIntervals;
import com.google.android.gms.common.api.ResultCallback;

import uk.ac.strath.contextualtriggers.ContextualTriggersService;
import uk.ac.strath.contextualtriggers.Logger;
import uk.ac.strath.contextualtriggers.data.TimeOfDayData;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class IntervalsDataManager extends AlarmDataManager<TimeOfDayData> {
    Logger logger;
    private final IBinder binder = new IntervalsDataManager.LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        setup();
        return binder;
    }

    public class LocalBinder extends Binder {
        public IDataManager getInstance() {
            return IntervalsDataManager.this;
        }
    }

    public IntervalsDataManager() {
        super(60, 600);
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

    /*This Could be setup to fire on a transition, instead of a poll*/
    private void monitor() {


// Call findCurrentPlace and handle the response (first check that the user has granted permission).
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Awareness.SnapshotApi.getTimeIntervals(ContextualTriggersService.getGoogleAPIClient())
                    .setResultCallback(new ResultCallback<TimeIntervalsResult>() {
                        @Override
                        public void onResult(@NonNull TimeIntervalsResult intervalResult) {
                            if (!intervalResult.getStatus().isSuccess()) {
                                Log.d("IntervalsDM", intervalResult.getStatus().toString());
                                Log.e("IntervalsDataManager", intervalResult.getStatus().getStatusMessage() + " ");
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
