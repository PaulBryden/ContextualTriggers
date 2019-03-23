package uk.ac.strath.contextualtriggers.conditions;

import uk.ac.strath.contextualtriggers.data.WeatherData;

/**
 * Condition satisfied if current weather matches a target value. Use constants defined in
 * WeatherService to represent weather states.
 */
public class WeatherCondition extends DataCondition<Integer> {

    private WeatherData targetWeather;

    public WeatherCondition(int targetWeather) {
        super(-1);
        this.targetWeather.TemperatureCelsius = targetWeather;
    }

    @Override
    public boolean isSatisfied() {
        if (getData() < 0) { // No weather updates received yet
            return false;
        }
        return getData() == targetWeather.TemperatureCelsius;
    }

}
