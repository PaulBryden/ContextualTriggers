package uk.ac.strath.contextualtriggers.conditions;

import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.android.gms.awareness.state.Weather;

import org.junit.Test;

import uk.ac.strath.contextualtriggers.actions.UnitTestAction;
import uk.ac.strath.contextualtriggers.data.WeatherData;
import uk.ac.strath.contextualtriggers.managers.DataManager;
import uk.ac.strath.contextualtriggers.managers.IDataManager;
import uk.ac.strath.contextualtriggers.triggers.Trigger;

import static org.junit.Assert.assertEquals;

public class ClearWeatherConditionUnitTest {

    @Test
    public void ClearWeatherConditionUnitTest() {
        class ClearWeatherMockManager extends DataManager<WeatherData> implements IDataManager<WeatherData> {
            boolean firstTime = true;

            @Nullable
            @Override
            public IBinder onBind(Intent intent) {
                return null;
            }

            public void mock() {
                WeatherData data = new WeatherData();
                if (firstTime) {
                    data.Conditions = new int[]{Weather.CONDITION_ICY};
                    firstTime = false;
                } else {
                    data.Conditions = new int[]{Weather.CONDITION_CLEAR};
                }
                sendUpdate(data);
            }
        }

        UnitTestAction action = new UnitTestAction();
        ClearWeatherMockManager manager = new ClearWeatherMockManager();
        ClearWeatherCondition altTransCondition = new ClearWeatherCondition(manager);
        Trigger.Builder T = new Trigger.Builder();
        T.setCondition(altTransCondition);
        T.setAction(action);
        Trigger trig = T.build();
        manager.mock();
        assertEquals(false, altTransCondition.isSatisfied());
        System.out.println("AcceptableTimeConditionUnitTest");
        manager.mock();
        assertEquals(true, altTransCondition.isSatisfied());
    }

}
