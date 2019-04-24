package uk.ac.strath.contextualtriggers.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class CalendarData extends AbstractData {


    public List<EventData> cd;

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


    @Override
    public boolean equals(Object o){
        if (o instanceof CalendarData){
            if(!super.equals(o) || cd.size() != ((CalendarData) o).cd.size()){
                return false;
            }
            for(int i = 0; i < cd.size() ; i++){
                if(!cd.get(i).equals(((CalendarData) o).cd.get(i))){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

}
