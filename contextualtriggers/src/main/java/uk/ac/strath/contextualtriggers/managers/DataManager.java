package uk.ac.strath.contextualtriggers.managers;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import uk.ac.strath.contextualtriggers.conditions.DataCondition;
import uk.ac.strath.contextualtriggers.data.Data;

public abstract class DataManager<T extends Data> extends Service implements IDataManager<T> {
    private List<DataCondition<T>> observers;
    private T cachedData;

    public DataManager() {
        observers = new ArrayList<DataCondition<T>>();
        cachedData = null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int res = super.onStartCommand(intent, flags, startId);
        registerBatteryBroadcastReceiver();
        return res;
    }

    @Override
    public void register(DataCondition<T> dataCondition) {
        observers.add(dataCondition);
        if (cachedData != null) {
            Log.d("DataManager", "Registering condition, cachedData = " + cachedData.toString());
            dataCondition.notifyUpdate(cachedData);
        }
    }

    protected void sendUpdate(T data) {
        cachedData = data;
        for (DataCondition<T> i : observers) {
            i.notifyUpdate(data);
        }
    }

    private void registerBatteryBroadcastReceiver() {
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                setLowPowerMode(intent.getBooleanExtra(BatteryDataManager.LPM_BOOL_NAME, false));
                Log.d("DataManager", this.getClass().toString() + ":  lpm " + intent.getBooleanExtra(BatteryDataManager.LPM_BOOL_NAME, false));
            }
        };
        lbm.registerReceiver(receiver, new IntentFilter(BatteryDataManager.LPM_ACTION_NAME));
    }

    @Override
    public void setLowPowerMode(boolean lpm) {
        // By default, do nothing
    }
}
