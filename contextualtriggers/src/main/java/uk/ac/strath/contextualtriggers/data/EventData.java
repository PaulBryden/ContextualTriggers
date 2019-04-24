package uk.ac.strath.contextualtriggers.data;

import java.util.Date;

public class EventData extends AbstractData {
    public String name;
    public Date time;

    public EventData(String name, Date time) {
        this.name = name;
        this.time = time;
    }


}
