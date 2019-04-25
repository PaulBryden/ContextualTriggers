package uk.ac.strath.contextualtriggers.conditions;

import com.google.android.gms.awareness.state.Weather;

import org.junit.Test;

import uk.ac.strath.contextualtriggers.data.WeatherData;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ClearWeatherConditionUnitTest {

    @Test
    public void testNoWeatherDataReceivedYet() {
        MockDataManager<WeatherData> manager = new MockDataManager<>();
        ClearWeatherCondition condition = new ClearWeatherCondition(manager);
        assertFalse(condition.isSatisfied());
    }

    @Test
    public void testClearConditionSatisfied() {
        MockDataManager<WeatherData> manager = new MockDataManager<>();
        ClearWeatherCondition condition = new ClearWeatherCondition(manager);
        manager.sendUpdate(new WeatherData(0.0, 0, new int[]{Weather.CONDITION_CLEAR}));
        assertTrue(condition.isSatisfied());
    }

    @Test
    public void testClearConditionSatisfiedMultiple() {
        MockDataManager<WeatherData> manager = new MockDataManager<>();
        ClearWeatherCondition condition = new ClearWeatherCondition(manager);
        manager.sendUpdate(new WeatherData(0.0, 0, new int[]{Weather.CONDITION_ICY, Weather.CONDITION_CLEAR}));
        assertTrue(condition.isSatisfied());
    }

    @Test
    public void testClearConditionNotSatisfied() {
        MockDataManager<WeatherData> manager = new MockDataManager<>();
        ClearWeatherCondition condition = new ClearWeatherCondition(manager);
        manager.sendUpdate(new WeatherData(0.0, 0, new int[]{Weather.CONDITION_CLEAR}));
        manager.sendUpdate(new WeatherData(0.0, 0, new int[]{Weather.CONDITION_ICY}));
        assertFalse(condition.isSatisfied());
    }

}
