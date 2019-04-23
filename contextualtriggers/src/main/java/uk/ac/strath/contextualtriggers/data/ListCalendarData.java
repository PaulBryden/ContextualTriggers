package uk.ac.strath.contextualtriggers.data;

import java.util.ArrayList;
import java.util.List;

public class ListCalendarData extends  AbstractData {

    private List<CalendarData> cd = new ArrayList<>();

    public ListCalendarData(List<CalendarData> cd){
        this.cd = cd;
    }
}
