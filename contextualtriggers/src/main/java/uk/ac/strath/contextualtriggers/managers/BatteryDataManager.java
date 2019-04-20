package uk.ac.strath.contextualtriggers.managers;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import uk.ac.strath.contextualtriggers.Logger;
import uk.ac.strath.contextualtriggers.data.BatteryData;
import uk.ac.strath.contextualtriggers.data.StepAndGoalData;
import uk.ac.strath.contextualtriggers.intentReceivers.BatteryLevelReceiver;
import uk.ac.strath.contextualtriggers.intentReceivers.StepAndGoalIntentReceiver;

public class BatteryDataManager extends DataManager<BatteryData> implements IDataManager<BatteryData> {

    Logger logger;
    BatteryData batteryData;
    private BatteryLevelReceiver receiver;
    private final IBinder binder = new LocalBinder();
    public class LocalBinder extends Binder {
        public IDataManager getInstance() {
            return BatteryDataManager.this;
        }
    }

    public BatteryDataManager(){
        Log.d("StepAndGoalIntentReceiver","Starting");
        setup();
    }

    private void configureReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.BATTERY_LOW");
        filter.addAction("android.intent.action.BATTERY_OKAY");
        receiver = new BatteryLevelReceiver();
        registerReceiver(receiver, filter);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //Not sure if this is required
        //Needed if onStartCommand not called automatically
        Log.d("BatteryDataManager", "Binding");
        configureReceiver();
        return binder;
    }

    private void setup() {
        batteryData = new BatteryData();
        logger = Logger.getInstance();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        monitor(intent);
        return START_STICKY;
    }

    private void monitor(Intent intent){

        batteryData.isLow = intent.getBooleanExtra("level", false);
        logger.log("Battery Low: " + batteryData.isLow);
        Log.d("BatteryDataManager", "Starting");
        sendUpdate(batteryData);
    }

}
