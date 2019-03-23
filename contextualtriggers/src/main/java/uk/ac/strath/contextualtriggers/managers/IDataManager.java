package uk.ac.strath.contextualtriggers.managers;

import java.util.ArrayList;

import uk.ac.strath.contextualtriggers.conditions.DataCondition;

public interface IDataManager<T>
{
    void register(DataCondition<T> dataCondition);
}
