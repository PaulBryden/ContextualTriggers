package uk.ac.strath.contextualtriggers.managers;

import java.util.ArrayList;

import uk.ac.strath.contextualtriggers.conditions.DataCondition;
import uk.ac.strath.contextualtriggers.data.Data;

public interface IDataManager<T extends Data>
{
    void register(DataCondition<T> dataCondition);
}
