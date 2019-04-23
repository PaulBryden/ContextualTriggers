package uk.ac.strath.contextualtriggers.managers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import uk.ac.strath.contextualtriggers.data.Data;

public abstract class AlarmDataManager<T extends Data> extends DataManager<T> {

    private int pollingPeriod; // in seconds
    private int lpmPollingPeriod; // in seconds
    private boolean lpm;

    public AlarmDataManager(int pollingPeriod, int lpmPollingPeriod) {
        this.pollingPeriod = pollingPeriod;
        this.lpmPollingPeriod = lpmPollingPeriod;
        this.lpm = false;
    }

    protected void alarm() {
        int period = lpm ? lpmPollingPeriod : pollingPeriod;
        if (period > 0) {
            AlarmManager alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            Intent ip = new Intent(this, this.getClass());
            PendingIntent alarmIntent = PendingIntent.getService(this, 0, ip, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + period * 1000, alarmIntent);
        }
    }

    @Override
    public void setLowPowerMode(boolean lpm) {
        this.lpm = lpm;
    }
}
