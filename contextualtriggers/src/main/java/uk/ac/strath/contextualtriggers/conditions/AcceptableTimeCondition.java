package uk.ac.strath.contextualtriggers.conditions;

import uk.ac.strath.contextualtriggers.data.TimeOfDayData;
import uk.ac.strath.contextualtriggers.managers.IDataManager;

/**
 * A condition that is satisfied if any of the current time intervals (like morning or afternoon)
 * match any of the target time intervals.
 */
public class AcceptableTimeCondition extends DataCondition<TimeOfDayData> {

    private TimeOfDayData targetIntervals;

    public AcceptableTimeCondition(TimeOfDayData targetIntervals, IDataManager<TimeOfDayData> dataManager) {
        super(dataManager, 60);
        this.targetIntervals = targetIntervals;
    }

    @Override
    public boolean isSatisfied() {
        if (getData() == null) {
            return false;
        }
        for (int i : getData().intervals) {
            if(targetIntervals!=null)
            {
                for (int x : targetIntervals.intervals)
                {
                    if (x == i)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
