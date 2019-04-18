package uk.ac.strath.contextualtriggers.conditions;

import uk.ac.strath.contextualtriggers.data.WeatherData;
import uk.ac.strath.contextualtriggers.managers.IDataManager;

/**
 * Condition satisfied if current weather matches a target value. Use constants defined in
 * WeatherService to represent weather states.
 */
public class WeatherSunnyCondition extends DataCondition<WeatherData> {

    private WeatherData targetWeather;

    public WeatherSunnyCondition(WeatherData targetWeather, IDataManager<WeatherData> dataManager) {
        super(targetWeather, dataManager);
        this.targetWeather = targetWeather;
    }

    @Override
    public boolean isSatisfied() {
        if (getData().TemperatureCelsius<targetWeather.TemperatureCelsius) { // No weather updates received yet
            return false;
        }
        return getData().TemperatureCelsius > targetWeather.TemperatureCelsius;
    }

}
