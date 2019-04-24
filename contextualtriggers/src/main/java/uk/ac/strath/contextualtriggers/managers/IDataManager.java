package uk.ac.strath.contextualtriggers.managers;

import uk.ac.strath.contextualtriggers.conditions.DataCondition;
import uk.ac.strath.contextualtriggers.data.Data;

public interface IDataManager<T extends Data>
{
    void register(DataCondition<T> dataCondition);
    void setLowPowerMode(boolean lpm);
}
