package uk.ac.strath.contextualtriggers;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.common.api.GoogleApiClient;

import uk.ac.strath.contextualtriggers.triggers.ButItsSunnyOutsideTrigger;

public class ContextualTriggersService extends Service {
    private static GoogleApiClient mGoogleApiClient;

    ButItsSunnyOutsideTrigger sunnyOotsideTrigger;
    public static GoogleApiClient getGoogleAPIClient()
    {
        return mGoogleApiClient;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //noinspection MissingPermission

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Awareness.API)
                .build();
        mGoogleApiClient.connect();
        //Will eventually be trigger builder
        sunnyOotsideTrigger = new ButItsSunnyOutsideTrigger();
        return START_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
