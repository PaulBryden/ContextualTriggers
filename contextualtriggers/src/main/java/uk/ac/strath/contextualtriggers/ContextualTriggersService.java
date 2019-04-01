package uk.ac.strath.contextualtriggers;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

import uk.ac.strath.contextualtriggers.managers.StepDataManager;
//import uk.ac.strath.contextualtriggers.triggers.ButItsSunnyOutsideTrigger;
import uk.ac.strath.contextualtriggers.triggers.DefaultTriggers;
import uk.ac.strath.contextualtriggers.triggers.ITrigger;

public class ContextualTriggersService extends Service {

    private static GoogleApiClient mGoogleApiClient;

    private static List<ITrigger> triggerList = new ArrayList<>();

    public static GoogleApiClient getGoogleAPIClient() {
        return mGoogleApiClient;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //noinspection MissingPermission
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Awareness.API)
                .build();
        mGoogleApiClient.connect();
        startDataManagers();
        return START_STICKY;
    }

    // These probably won't be needed
    public static void addTrigger(ITrigger t) {
        triggerList.add(t);
    }

    public static void removeTrigger(ITrigger t) {
        triggerList.remove(t);
    }


    private void startDataManagers() {
        //Pointless Trigger creation
        Log.d("TriggerManager", "Creating default triggers");
        Intent i = new Intent(this, StepDataManager.class);
        boolean b = bindService(i, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d("TriggerManager", "Connected to StepDataManager");
                triggerList.add(DefaultTriggers.createStepMonitorTrigger(service));
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, Context.BIND_AUTO_CREATE);
        startService(i);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
