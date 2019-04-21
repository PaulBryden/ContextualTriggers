package uk.ac.strath.contextualtriggers.conditions;

import com.google.android.gms.awareness.state.TimeIntervals;
import com.google.android.gms.awareness.state.Weather;

import java.util.Arrays;
import java.util.Collections;

import uk.ac.strath.contextualtriggers.data.WeatherData;
import uk.ac.strath.contextualtriggers.managers.IDataManager;

/**
 * Condition satisfied if current weather matches a target value. Use constants defined in
 * WeatherService to represent weather states.
 */
public class AcceptableTimeCondition extends DataCondition<TimeIntervals> {

    private TimeIntervals targetIntervals;

    public AcceptableTimeCondition(TimeIntervals targetIntervals, IDataManager<TimeIntervals> dataManager) {
        super(targetIntervals, dataManager);
        this.targetIntervals = targetIntervals;
    }

    @Override
    public boolean isSatisfied() {
        return(Collections.indexOfSubList(Arrays.asList(getData().getTimeIntervals()),Arrays.asList(targetIntervals.getTimeIntervals()))>0);

    }

}
