package uk.ac.strath.contextualtriggers.data;

import java.lang.reflect.Type;

public class AltitudeData extends  AbstractData
{
    public double altitude;

    @Override
    public boolean equals(Object o){
        if(o instanceof AltitudeData){
            return ((AltitudeData) o).altitude == (this.altitude) && super.equals(o);
        }
        return false;
    }


    public static Type getType() {
        return AltitudeData.class;
    }
}
