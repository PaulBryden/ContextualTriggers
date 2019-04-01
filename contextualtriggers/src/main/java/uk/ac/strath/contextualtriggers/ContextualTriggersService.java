package uk.ac.strath.contextualtriggers;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;

import uk.ac.strath.contextualtriggers.triggers.ButItsSunnyOutsideTrigger;
import uk.ac.strath.contextualtriggers.triggers.ITrigger;

public class ContextualTriggersService extends Service {
    private static GoogleApiClient mGoogleApiClient;

    private static List<ITrigger> triggerList;
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

        return START_STICKY;
    }

   //ITrigger can be changed to Trigger if ITrigger not required
   public static void addTrigger(ITrigger t){
        triggerList.add(t);
   }

   public static void removeTrigger(ITrigger t){
       triggerList.remove(t);
   }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
