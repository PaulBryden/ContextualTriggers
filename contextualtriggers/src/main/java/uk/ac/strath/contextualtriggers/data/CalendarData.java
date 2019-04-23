package uk.ac.strath.contextualtriggers.data;

import java.util.Date;

public class CalendarData extends AbstractData

{
    public String name;
    public Date time;

    public CalendarData(String name, Date time) {
        this.name = name;
        this.time = time;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof CalendarData){
            System.out.println(((CalendarData) o).name + " " + this.name);
            System.out.println(((CalendarData) o).time + " " + this.time);
            return  ((CalendarData) o).name.equals(this.name) &&
                    ((CalendarData) o).time.equals(this.time) &&
                    super.equals(o);
        }
        return false;
    }

}
