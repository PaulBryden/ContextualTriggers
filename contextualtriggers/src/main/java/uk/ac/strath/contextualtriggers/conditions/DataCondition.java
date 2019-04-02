package uk.ac.strath.contextualtriggers.conditions;

import android.util.Log;

import uk.ac.strath.contextualtriggers.managers.IDataManager;

public abstract class DataCondition<T> extends AbstractCondition {

    private T data;

    DataCondition(IDataManager<T> dataManager) {
        data = null;
        dataManager.register(this);
    }

    @Deprecated
    DataCondition() {
        // placeholder - change conditions to use data managers
    }

    DataCondition(T initialData, IDataManager<T> dataManager) {
        this(dataManager);
        data = initialData;
    }

    public T getData() {
        return data;
    }

    public void notifyUpdate(T data) {
        this.data = data;
        try
        {
            getTrigger().notifyChange();
        }
        catch(NullPointerException e)
        {
            Log.d("DataCondition", "Trigger Not Attached");

        }
    }

}
