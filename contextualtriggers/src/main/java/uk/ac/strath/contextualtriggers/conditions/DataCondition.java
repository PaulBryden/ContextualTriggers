package uk.ac.strath.contextualtriggers.conditions;

import android.support.annotation.Nullable;
import android.util.Log;

import uk.ac.strath.contextualtriggers.data.Data;
import uk.ac.strath.contextualtriggers.exceptions.TriggerNotConnectedException;
import uk.ac.strath.contextualtriggers.managers.IDataManager;

public abstract class DataCondition<T extends Data> extends AbstractCondition {

    @Nullable
    private T data;
    private int dataTimeout; // in minutes

    DataCondition(IDataManager<T> dataManager) {
        this(dataManager, 0, null);
    }

    DataCondition(IDataManager<T> dataManager, int dataTimeout) {
        this(dataManager, dataTimeout, null);
    }

    @Deprecated
    DataCondition() {
        // placeholder - change conditions to use data managers
    }

    DataCondition(IDataManager<T> dataManager, int dataTimeout, T initialData) {
        data = initialData;
        this.dataTimeout = dataTimeout;
        dataManager.register(this);
    }

    @Nullable
    public T getData() {
        return data;
    }

    public boolean hasStaleData() {
        if (data == null) {
            return false;
        }
        return data.getTimestamp() < System.currentTimeMillis() - (dataTimeout * 60000);
    }

    public void notifyUpdate(T data) {
        this.data = data;
        Log.d("Condition", this.getClass().getSimpleName() + " received update (" + data.toString() + ")");
        Log.d("Condition", this.getClass().getSimpleName() + " satisfied: " + this.isSatisfied());
        try {
            getTrigger().notifyChange();
        } catch (TriggerNotConnectedException e) {
            // Do nothing, as this is expected behaviour during initialisation.
        }
    }

}
