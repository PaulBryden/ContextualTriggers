package uk.ac.strath.contextualtriggers.managers;
import java.util.ArrayList;
import java.util.List;

import uk.ac.strath.contextualtriggers.conditions.*;
public class DataManager<T> implements IDataManager<T>
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
