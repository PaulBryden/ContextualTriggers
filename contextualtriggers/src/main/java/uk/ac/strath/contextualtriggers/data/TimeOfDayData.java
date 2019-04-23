package uk.ac.strath.contextualtriggers.data;

import java.lang.reflect.Array;
import java.util.Arrays;

public class TimeOfDayData extends AbstractData {
    public int[] intervals;

    public TimeOfDayData(int[] intervals) {
        this.intervals = intervals;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof TimeOfDayData){
            return Arrays.equals(((TimeOfDayData) o).intervals, this.intervals) && super.equals(o);
        }
        return false;
    }
}
