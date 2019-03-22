package uk.ac.strath.contextualtriggers.conditions;

/**
 * Condition satisfied if current weather matches a target value. Use constants defined in
 * WeatherService to represent weather states.
 */
public class WeatherCondition extends DataCondition<Integer> {

    private int targetWeather;

    public WeatherCondition(int targetWeather) {
        super(-1);
        this.targetWeather = targetWeather;
    }

    @Override
    public boolean isSatisfied() {
        if (getData() < 0) { // No weather updates received yet
            return false;
        }
        return getData() == targetWeather;
    }

}
