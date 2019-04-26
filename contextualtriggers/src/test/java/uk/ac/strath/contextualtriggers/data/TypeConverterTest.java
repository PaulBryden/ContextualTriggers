package uk.ac.strath.contextualtriggers.data;

import com.google.android.gms.location.DetectedActivity;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static junit.framework.Assert.assertEquals;

public class TypeConverterTest {
    @Test
    public void testActivityData() {
        Data d = new ActivityData(new DetectedActivity(1, 1));
        String s = DataConverter.DataToString(d);
        Data d2 = DataConverter.StringToData(s);
        assertEquals(ActivityData.class, d2.getClass());
        assertEquals(d, d2);
    }

    @Test
    public void testAltitudeData() {
        AltitudeData d = new AltitudeData(12345);
        String s = DataConverter.DataToString(d);
        Data d2 = DataConverter.StringToData(s);
        assertEquals(AltitudeData.class, d2.getClass());
        assertEquals(d, d2);
    }

    @Test
    public void testBatteryData() {
        Data d = new BatteryData();
        String s = DataConverter.DataToString(d);
        Data d2 = DataConverter.StringToData(s);
        assertEquals(BatteryData.class, d2.getClass());
        assertEquals(d, d2);
    }

    @Test
    public void testCalendarData() {
        CalendarData d = new CalendarData(new ArrayList<>());
        d.cd.add(new EventData("event1", new Date(1970, 1, 1)));
        d.cd.add(new EventData("event2", new Date(2001, 1, 1)));
        d.cd.add(new EventData("event3", new Date(2020, 6, 6)));
        String s = DataConverter.DataToString(d);
        Data d2 = DataConverter.StringToData(s);
        assertEquals(CalendarData.class, d2.getClass());
        assertEquals(d, d2);
    }

    @Test
    public void testListCalendarData() {
        Data d = new ListCalendarData(new ArrayList<>());
        String s = DataConverter.DataToString(d);
        Data d2 = DataConverter.StringToData(s);
        assertEquals(ListCalendarData.class, d2.getClass());
        assertEquals(d, d2);
    }

    @Test
    public void testNotificationData() {
        Data d = new NotificationData();
        String s = DataConverter.DataToString(d);
        Data d2 = DataConverter.StringToData(s);
        assertEquals(NotificationData.class, d2.getClass());
        assertEquals(d, d2);
    }

    @Test
    public void testPlacesData() {
        Data d = new PlacesData(new ArrayList<>());
        String s = DataConverter.DataToString(d);
        Data d2 = DataConverter.StringToData(s);
        assertEquals(PlacesData.class, d2.getClass());
        assertEquals(d, d2);
    }

    @Test
    public void testStepAndGoalData() {
        StepAndGoalData d = new StepAndGoalData();

        String s = DataConverter.DataToString(d);
        Data d2 = DataConverter.StringToData(s);

        assertEquals(StepAndGoalData.class, d2.getClass());
        assertEquals(d, d2);
    }

    @Test
    public void testTimeOfDayData() {
        Data d = new TimeOfDayData(new int[10]);
        String s = DataConverter.DataToString(d);
        Data d2 = DataConverter.StringToData(s);
        assertEquals(TimeOfDayData.class, d2.getClass());
        assertEquals(d, d2);
    }

    @Test
    public void testVoidData() {
        Data d = new VoidData();
        String s = DataConverter.DataToString(d);
        Data d2 = DataConverter.StringToData(s);
        assertEquals(VoidData.class, d2.getClass());
        assertEquals(d, d2);
    }

    @Test
    public void testWeatherData() {
        Data d = new WeatherData(0.0f, 0, new int[0]);
        String s = DataConverter.DataToString(d);
        Data d2 = DataConverter.StringToData(s);
        assertEquals(WeatherData.class, d2.getClass());
        assertEquals(d, d2);
    }

}
