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
import com.google.android.gms.awareness.snapshot.TimeIntervalsResponse;
import com.google.android.gms.awareness.state.TimeIntervals;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import uk.ac.strath.contextualtriggers.data.TimeOfDayData;
import uk.ac.strath.contextualtriggers.permissions.RequestLocationPermission;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class IntervalsDataManager extends AlarmDataManager<TimeOfDayData> {
    private final IBinder binder = new IntervalsDataManager.LocalBinder();

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

        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {   // Permission is not granted
            // Should we show an explanation?
            Intent i = new Intent(this, RequestLocationPermission.class);
            startActivity(i);
        } else {
            Awareness.getSnapshotClient(this).getTimeIntervals().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("IntervalsDataManager", e.getMessage());
                }
            }).addOnSuccessListener(new OnSuccessListener<TimeIntervalsResponse>() {
                @Override
                public void onSuccess(TimeIntervalsResponse timeIntervalsResponse) {
                    // parse and display current weather status
                    TimeIntervals intervals = timeIntervalsResponse.getTimeIntervals();
                    Log.d("IntervalsDM", intervals.toString());
                    sendUpdate(new TimeOfDayData(intervals.getTimeIntervals()));
                }
            });
        }
    }
}
