package uk.ac.strath.contextualtriggers.managers;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import uk.ac.strath.contextualtriggers.data.BatteryData;
import uk.ac.strath.contextualtriggers.intentReceivers.BatteryLevelReceiver;

public class BatteryDataManager extends DataManager<BatteryData> {

    public static final String LPM_BOOL_NAME = "LPM";
    public static final String LPM_ACTION_NAME = "MANAGERS/LPM";

    BatteryData batteryData;
    private BatteryLevelReceiver receiver;
    private final IBinder binder = new LocalBinder();

    @Override
    public void setLowPowerMode(boolean lpm) {
        // This one should always run as normal
    }

    public class LocalBinder extends Binder {
        public IDataManager getInstance() {
            return BatteryDataManager.this;
        }
    }

    public BatteryDataManager() {
        Log.d("StepAndGoalIntentReceiver", "Starting");
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
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        monitor(intent);
        return START_STICKY;
    }

    private void monitor(Intent intent) {
        batteryData.isLow = intent.getBooleanExtra("level", false);
        Log.d("BatteryDataManager", "Battery low: " + batteryData.isLow);
        sendUpdate(batteryData);
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
        Intent i = new Intent();
        i.setAction(LPM_ACTION_NAME);
        i.putExtra(LPM_BOOL_NAME, batteryData.isLow);
        lbm.sendBroadcast(i);
    }

}
