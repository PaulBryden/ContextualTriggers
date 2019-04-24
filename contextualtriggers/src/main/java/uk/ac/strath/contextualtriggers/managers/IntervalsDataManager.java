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
import com.google.android.gms.awareness.snapshot.TimeIntervalsResult;
import com.google.android.gms.awareness.state.TimeIntervals;
import com.google.android.gms.common.api.ResultCallback;

import uk.ac.strath.contextualtriggers.ContextualTriggersService;
import uk.ac.strath.contextualtriggers.MainApplication;
import uk.ac.strath.contextualtriggers.permissions.RequestLocationPermission;
import uk.ac.strath.contextualtriggers.data.TimeOfDayData;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class IntervalsDataManager extends AlarmDataManager<TimeOfDayData> {
    private final IBinder binder = new IntervalsDataManager.LocalBinder();

    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        setup();
        return binder;
    }

    public class LocalBinder extends Binder {
        public IDataManager<TimeOfDayData> getInstance() {
            return IntervalsDataManager.this;
        }
    }

    public IntervalsDataManager() {
        super(60, 600);
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

    /*This Could be setup to fire on a transition, instead of a poll*/
    private void monitor() {

        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
               {   // Permission is not granted
                   // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainApplication.getAppActivity(),
                            ACCESS_FINE_LOCATION)) {
                        Intent i = new Intent(this, RequestLocationPermission.class);
                        startActivity(i);

                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(MainApplication.getAppActivity(),
                                new String[]{ACCESS_FINE_LOCATION},
                                MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                    }
                } else {
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
