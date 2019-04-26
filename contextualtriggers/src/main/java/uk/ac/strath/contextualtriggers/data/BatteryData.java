package uk.ac.strath.contextualtriggers.data;

import java.lang.reflect.Type;

public class BatteryData extends AbstractData {
    public boolean isLow;

    @Override
    public boolean equals(Object o) {
        if (o instanceof BatteryData) {
            return ((BatteryData) o).isLow == (this.isLow) && super.equals(o);
        }
        return false;
    }


    public static Type getType() {
        return BatteryData.class;
    }
}
