package uk.ac.strath.contextualtriggers.data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class CalendarData extends AbstractData {

    public List<EventData> cd = new ArrayList<>();

    public CalendarData(List<EventData> cd) {
        // Sort events in ascending time order.
        cd.sort(Comparator.comparing(o -> o.time));
        this.cd = cd;
    }

    /**
     * Gets every event in the calendar.
     *
     * @return events
     */
    public List<EventData> getEvents() {
        return cd;
    }

    /**
     * Gets any event(s) in the calendar between two times.
     *
     * @param from lower bound
     * @param to   upper bound
     * @return events between lower and upper bounds
     */
    public List<EventData> getEventsBetween(Date from, Date to) {
        List<EventData> events = new ArrayList<>();
        for (EventData event : cd) {
            if (event.time.after(from) && event.time.before(to)) {
                events.add(event);
            }
        }
        return events;
    }

}
