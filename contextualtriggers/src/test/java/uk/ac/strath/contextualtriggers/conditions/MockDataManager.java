package uk.ac.strath.contextualtriggers.conditions;

import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import uk.ac.strath.contextualtriggers.data.Data;
import uk.ac.strath.contextualtriggers.managers.DataManager;

public class MockDataManager<T extends Data> extends DataManager<T> {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void sendUpdate(T data) {
        for (DataCondition<T> i : observers) {
            i.notifyUpdate(data);
        }
    }
}
