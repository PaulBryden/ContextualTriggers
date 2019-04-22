package uk.ac.strath.contextualtriggers;

import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.location.DetectedActivity;

import org.junit.Test;

import uk.ac.strath.contextualtriggers.actions.UnitTestAction;
import uk.ac.strath.contextualtriggers.conditions.AcceptableTimeCondition;
import uk.ac.strath.contextualtriggers.conditions.ActivityPeriodCondition;
import uk.ac.strath.contextualtriggers.conditions.AltitudeTransitionCondition;
import uk.ac.strath.contextualtriggers.conditions.ClearWeatherCondition;
import uk.ac.strath.contextualtriggers.data.AltitudeData;
import uk.ac.strath.contextualtriggers.data.WeatherData;
import uk.ac.strath.contextualtriggers.managers.DataManager;
import uk.ac.strath.contextualtriggers.managers.IDataManager;
import uk.ac.strath.contextualtriggers.triggers.Trigger;

import static com.google.android.gms.awareness.fence.TimeFence.TIME_INTERVAL_AFTERNOON;
import static com.google.android.gms.awareness.fence.TimeFence.TIME_INTERVAL_MORNING;
import static com.google.android.gms.location.DetectedActivity.STILL;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ConditionUnitTest
{

    @Test
    public void AcceptableTimeConditionUnitTest() {
        class TimeIntervalsDataManager extends DataManager<int[]> implements IDataManager<int[]>
        {
            @Nullable
            @Override
            public IBinder onBind(Intent intent)
            {
                return null;
            }
            public void mock()
            {int [] test = {TIME_INTERVAL_MORNING, TIME_INTERVAL_AFTERNOON};
                sendUpdate(test);
            }
        }

        UnitTestAction action = new UnitTestAction();
        TimeIntervalsDataManager manager = new TimeIntervalsDataManager();
        int[] desiredTimeInterval = {TIME_INTERVAL_AFTERNOON};
        AcceptableTimeCondition periodCondition = new AcceptableTimeCondition(desiredTimeInterval,manager);
        Trigger.Builder T = new Trigger.Builder();
        T.setCondition(periodCondition);
        T.setAction(action);
        Trigger trig = T.build();
        manager.mock();
        assertEquals(true,periodCondition.isSatisfied());
        System.out.println("AcceptableTimeConditionUnitTest");
        manager.mock();
        assertEquals(true,periodCondition.isSatisfied());
    }

    @Test
    public void ActivityPeriodConditionUnitTest() {
        class ActivityDataManager extends DataManager<DetectedActivity> implements IDataManager<DetectedActivity>
        {
            @Nullable
            @Override
            public IBinder onBind(Intent intent)
            {
                return null;
            }
            public void mock()
            {
                sendUpdate(new DetectedActivity(STILL,100));
            }
        }

        UnitTestAction action = new UnitTestAction();
        ActivityDataManager manager = new ActivityDataManager();
        ActivityPeriodCondition periodCondition = new ActivityPeriodCondition(10000, STILL,manager);
        Trigger.Builder T = new Trigger.Builder();
        T.setCondition(periodCondition);
        T.setAction(action);
        Trigger trig = T.build();
        manager.mock();
        assertEquals(false,periodCondition.isSatisfied());
        System.out.println("ActivityPeriodConditionUnitTest");
        try
        {
            Thread.sleep(10500);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        manager.mock();
        assertEquals(true,periodCondition.isSatisfied());
    }


    @Test
    public void AltitudeTransitionConditionUnitTest() {
        class AltitudeMockManager extends DataManager<AltitudeData> implements IDataManager<AltitudeData>
        {
            boolean firstTime=true;
            @Nullable
            @Override
            public IBinder onBind(Intent intent)
            {
                return null;
            }
            public void mock()
            {
                AltitudeData data = new AltitudeData();
                if(firstTime)
                {
                    data.altitude = 0;
                    firstTime=false;
                }
                else
                {
                    data.altitude = 20;
                }
                sendUpdate(data);
            }
        }

        UnitTestAction action = new UnitTestAction();
        AltitudeMockManager manager = new AltitudeMockManager();
        AltitudeTransitionCondition altTransCondition = new AltitudeTransitionCondition(19,manager);
        Trigger.Builder T = new Trigger.Builder();
        T.setCondition(altTransCondition);
        T.setAction(action);
        Trigger trig = T.build();
        manager.mock();
        assertEquals(false,altTransCondition.isSatisfied());
        System.out.println("AcceptableTimeConditionUnitTest");
        manager.mock();
        assertEquals(true,altTransCondition.isSatisfied());
    }


    @Test
    public void ClearWeatherConditionUnitTest() {
        class ClearWeatherMockManager extends DataManager<WeatherData> implements IDataManager<WeatherData>
        {
            boolean firstTime=true;
            @Nullable
            @Override
            public IBinder onBind(Intent intent)
            {
                return null;
            }
            public void mock()
            {
                WeatherData data = new WeatherData();
                if(firstTime)
                {
                    data.Conditions = new int[]{Weather.CONDITION_ICY};
                    firstTime=false;
                }
                else
                {
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
        assertEquals(false,altTransCondition.isSatisfied());
        System.out.println("AcceptableTimeConditionUnitTest");
        manager.mock();
        assertEquals(true,altTransCondition.isSatisfied());
    }

}