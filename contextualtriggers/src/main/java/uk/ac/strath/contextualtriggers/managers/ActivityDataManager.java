package uk.ac.strath.contextualtriggers.managers;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.DetectedActivityResponse;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import uk.ac.strath.contextualtriggers.data.ActivityData;

public class ActivityDataManager extends AlarmDataManager<ActivityData> {
    private final IBinder binder = new ActivityDataManager.LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        setup();
        return binder;
    }

    public class LocalBinder extends Binder {
        public IDataManager<ActivityData> getInstance() {
            return ActivityDataManager.this;
        }
    }

    public ActivityDataManager() {
        super(5, 60);
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
        // Permission has already been granted
        Awareness.getSnapshotClient(this).getDetectedActivity().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("ActivityDataManager", e.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<DetectedActivityResponse>() {
            @Override
            public void onSuccess(DetectedActivityResponse detectedActivityResponse) {
                ActivityRecognitionResult ar = detectedActivityResponse.getActivityRecognitionResult();
                DetectedActivity probableActivity = ar.getMostProbableActivity();
                Log.d("ActivityDataManager", probableActivity.toString());
                sendUpdate(new ActivityData(probableActivity));
            }
        });
    }

}

