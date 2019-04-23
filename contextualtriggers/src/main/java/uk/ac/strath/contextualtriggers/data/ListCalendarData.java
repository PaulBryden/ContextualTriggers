package uk.ac.strath.contextualtriggers.data;

import java.util.ArrayList;
import java.util.List;

public class ListCalendarData extends  AbstractData {

    private List<CalendarData> cd = new ArrayList<>();

    public ListCalendarData(List<CalendarData> cd){
        this.cd = cd;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof ListCalendarData){
            return ((ListCalendarData) o).cd.equals(this.cd) && super.equals(o);
        }
        return false;
    }
}
