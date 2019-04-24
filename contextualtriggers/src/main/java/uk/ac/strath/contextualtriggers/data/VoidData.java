package uk.ac.strath.contextualtriggers.data;

import java.lang.reflect.Type;

/**
 * Wrapper for Void data type with timestamp
 */
public class VoidData extends AbstractData {
    @Override
    public boolean equals(Object o){
        if(o instanceof VoidData){
            return super.equals(o);
        }
        return false;
    }

    public static Type getType() {
        return VoidData.class;
    }

    public VoidData() {
        super();
    }

    public VoidData(long timestamp) {
        super(timestamp);
    }

}
