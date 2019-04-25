package uk.ac.strath.contextualtriggers.conditions;

import com.google.android.gms.awareness.state.Weather;

import org.junit.Before;
import org.junit.Test;

import uk.ac.strath.contextualtriggers.data.WeatherData;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ClearWeatherConditionUnitTest {

    private MockDataManager<WeatherData> manager;
    private ClearWeatherCondition condition;

    @Before
    public void setup() {
        manager = new MockDataManager<>();
        condition = new ClearWeatherCondition(manager);
    }


    @Test
    public void testNoWeatherDataReceivedYet() {
        assertFalse(condition.isSatisfied());
    }

    @Test
    public void testClearConditionSatisfied() {
        manager.sendUpdate(new WeatherData(0.0, 0, new int[]{Weather.CONDITION_CLEAR}));
        assertTrue(condition.isSatisfied());
    }

    @Test
    public void testClearConditionSatisfiedMultiple() {
        manager.sendUpdate(new WeatherData(0.0, 0, new int[]{Weather.CONDITION_ICY, Weather.CONDITION_CLEAR}));
        assertTrue(condition.isSatisfied());
    }

    @Test
    public void testClearConditionNotSatisfied() {
        manager.sendUpdate(new WeatherData(0.0, 0, new int[]{Weather.CONDITION_CLEAR}));
        manager.sendUpdate(new WeatherData(0.0, 0, new int[]{Weather.CONDITION_ICY}));
        assertFalse(condition.isSatisfied());
    }

}
