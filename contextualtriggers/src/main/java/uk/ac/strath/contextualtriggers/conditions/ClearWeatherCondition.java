package uk.ac.strath.contextualtriggers.conditions;

import com.google.android.gms.awareness.state.Weather;

import uk.ac.strath.contextualtriggers.data.WeatherData;
import uk.ac.strath.contextualtriggers.managers.IDataManager;

/**
 * Condition satisfied if current weather matches a target value. Use constants defined in
 * WeatherService to represent weather states.
 */
public class ClearWeatherCondition extends DataCondition<WeatherData> {

    public ClearWeatherCondition(IDataManager<WeatherData> dataManager) {
        super(dataManager, 3 * 60);
    }

    @Override
    public boolean isSatisfied() {
        if (getData() == null) {
            return false;
        }
        for (int i : getData().getConditions()) {
            if (i == Weather.CONDITION_CLEAR) {
                return true;
            }
        }
        return false;
    }

}
