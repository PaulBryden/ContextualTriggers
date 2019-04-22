package uk.ac.strath.contextualtriggers.data;

import java.util.Date;

public class CalendarData extends  AbstractData
{
    public String name;
    public Date time;

    public CalendarData(String name, Date time)
    {
        this.name = name;
        this.time = time;
    }


}
