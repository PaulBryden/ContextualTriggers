package uk.ac.strath.contextualtriggers.data;

import java.util.Date;

public class EventData extends AbstractData {
    public String name;
    public Date time;

    public EventData(String name, Date time) {
        this.name = name;
        this.time = time;
    }



    @Override
    public boolean equals(Object o){
        if(o instanceof EventData){
            System.out.println(((EventData) o).name + " " + this.name);
            System.out.println(((EventData) o).time + " " + this.time);
            return  ((EventData) o).name.equals(this.name) &&
                    ((EventData) o).time.equals(this.time) &&
                    super.equals(o);
        }
        return false;
    }
}
