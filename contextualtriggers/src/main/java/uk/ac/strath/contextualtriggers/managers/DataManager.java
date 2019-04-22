package uk.ac.strath.contextualtriggers.managers;

import android.app.Service;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import uk.ac.strath.contextualtriggers.conditions.*;
import uk.ac.strath.contextualtriggers.data.Data;

public abstract class DataManager<T extends Data> extends Service implements IDataManager<T> {
    private List<DataCondition<T>> observers;
    private T cachedData;

    public DataManager() {
        observers = new ArrayList<DataCondition<T>>();
        cachedData = null;
    }

    public void register(DataCondition<T> dataCondition) {
        observers.add(dataCondition);
        if(cachedData!=null)
        {
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
}
