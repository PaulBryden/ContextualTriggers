package uk.ac.strath.contextualtriggers.conditions;

import java.util.List;

import uk.ac.strath.contextualtriggers.data.TimeOfDayData;
import uk.ac.strath.contextualtriggers.managers.IDataManager;

/**
 * Condition satisfied if current weather matches a target value. Use constants defined in
 * WeatherService to represent weather states.
 */
public class AcceptableTimeCondition extends DataCondition<TimeOfDayData> {

    private TimeOfDayData targetIntervals;

    public AcceptableTimeCondition(TimeOfDayData targetIntervals, IDataManager<TimeOfDayData> dataManager)
    {
        super(dataManager, 60);
        this.targetIntervals = targetIntervals;
    }

    @Override
    public boolean isSatisfied()
    {
        for (int i : getData().intervals)
        {
            for(int x : targetIntervals.intervals)
            {
                if (x==i)
                {
                    return true;
                }
            }
        }
        return false;
    }

}
