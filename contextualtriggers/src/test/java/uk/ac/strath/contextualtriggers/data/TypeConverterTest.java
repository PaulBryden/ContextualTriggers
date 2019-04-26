package uk.ac.strath.contextualtriggers.data;

import com.google.android.gms.location.DetectedActivity;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static junit.framework.Assert.assertEquals;

public class TypeConverterTest {
    @Test
    public void ActivityTest() {
        Data d = new ActivityData(new DetectedActivity(1, 1));
        String s = DataConverter.DataToString(d);
        Data d2 = DataConverter.StringToData(s);
        assertEquals(ActivityData.class, d2.getClass());
        assertEquals(d, d2);
    }

    @Test
    public void AltitudeTest() {
        AltitudeData d = new AltitudeData(12345);
        String s = DataConverter.DataToString(d);
        Data d2 = DataConverter.StringToData(s);
        assertEquals(AltitudeData.class, d2.getClass());
        assertEquals(d, d2);
    }

    @Test
    public void BatteryTest() {
        Data d = new BatteryData();
        String s = DataConverter.DataToString(d);
        Data d2 = DataConverter.StringToData(s);
        assertEquals(BatteryData.class, d2.getClass());
        assertEquals(d, d2);
    }

    @Test
    public void CalendarTest() {
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
    public void ListCalendarTest() {
        Data d = new ListCalendarData(new ArrayList<>());
        String s = DataConverter.DataToString(d);
        Data d2 = DataConverter.StringToData(s);
        assertEquals(ListCalendarData.class, d2.getClass());
        assertEquals(d, d2);
    }

    @Test
    public void NotificationTest() {
        Data d = new NotificationData();
        String s = DataConverter.DataToString(d);
        Data d2 = DataConverter.StringToData(s);
        assertEquals(NotificationData.class, d2.getClass());
        assertEquals(d, d2);
    }

    @Test
    public void PlacesTest() {
        Data d = new PlacesData(new ArrayList<>());
        String s = DataConverter.DataToString(d);
        Data d2 = DataConverter.StringToData(s);
        assertEquals(PlacesData.class, d2.getClass());
        assertEquals(d, d2);
    }

    @Test
    public void StepAndGoalTest() {
        // TODO: Insert dummy data
        StepAndGoalData d = new StepAndGoalData();

        String s = DataConverter.DataToString(d);
        Data d2 = DataConverter.StringToData(s);

        for (String k : d.getHistory().keySet()) {
            System.out.println(k);
            System.out.println(d.getHistory().get(k));

            System.out.println(((StepAndGoalData) d2).getHistory().get(k));

        }
        System.out.println(d.getHistory());

        System.out.println(((StepAndGoalData) d2).getHistory());

        assertEquals(StepAndGoalData.class, d2.getClass());
        assertEquals(d, d2);
    }

    @Test
    public void TimeOfDayTest() {
        Data d = new TimeOfDayData(new int[10]);
        String s = DataConverter.DataToString(d);
        Data d2 = DataConverter.StringToData(s);
        assertEquals(TimeOfDayData.class, d2.getClass());
        assertEquals(d, d2);
    }

    @Test
    public void VoidTest() {
        Data d = new VoidData();
        String s = DataConverter.DataToString(d);
        Data d2 = DataConverter.StringToData(s);
        assertEquals(VoidData.class, d2.getClass());
        assertEquals(d, d2);
    }

    @Test
    public void WeatherTest() {
        Data d = new WeatherData(0.0f, 0, new int[0]);
        String s = DataConverter.DataToString(d);
        Data d2 = DataConverter.StringToData(s);
        assertEquals(WeatherData.class, d2.getClass());
        assertEquals(d, d2);
    }

}
