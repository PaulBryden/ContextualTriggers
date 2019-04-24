package uk.ac.strath.contextualtriggers.conditions;

import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.android.gms.awareness.state.Weather;

import org.junit.Test;

import uk.ac.strath.contextualtriggers.data.WeatherData;
import uk.ac.strath.contextualtriggers.managers.DataManager;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ClearWeatherConditionUnitTest {

    private class MockWeatherDataManager extends DataManager<WeatherData> {

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        public void sendMockUpdate(int... conditions) {
            WeatherData data = new WeatherData(0, 0, conditions);
            sendUpdate(data);
        }
    }

    @Test
    public void testNoWeatherDataReceivedYet() {
        MockWeatherDataManager manager = new MockWeatherDataManager();
        ClearWeatherCondition condition = new ClearWeatherCondition(manager);
        assertFalse(condition.isSatisfied());
    }

    @Test
    public void testClearConditionSatisfied() {
        MockWeatherDataManager manager = new MockWeatherDataManager();
        ClearWeatherCondition condition = new ClearWeatherCondition(manager);
        manager.sendMockUpdate(Weather.CONDITION_CLEAR);
        assertTrue(condition.isSatisfied());
    }

    @Test
    public void testClearConditionNotSatisfied() {
        MockWeatherDataManager manager = new MockWeatherDataManager();
        ClearWeatherCondition condition = new ClearWeatherCondition(manager);
        manager.sendMockUpdate(Weather.CONDITION_CLEAR);
        manager.sendMockUpdate(Weather.CONDITION_ICY);
        assertFalse(condition.isSatisfied());
    }

}
