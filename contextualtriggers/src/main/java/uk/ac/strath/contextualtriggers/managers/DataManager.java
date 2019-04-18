package uk.ac.strath.contextualtriggers.managers;
import android.app.Service;

import java.util.ArrayList;
import java.util.List;

import uk.ac.strath.contextualtriggers.conditions.*;
public abstract class DataManager<T> extends Service implements IDataManager<T>
{
    private List<DataCondition<T>> observers;

    public DataManager()
    {
        observers=new ArrayList<DataCondition<T>>();
    }

    public void register(DataCondition<T> dataCondition)
    {
        observers.add(dataCondition);
    }

    protected void sendUpdate(T data)
    {
         for(DataCondition<T> i : observers)
         {
             i.notifyUpdate(data);
         }
    }
}
