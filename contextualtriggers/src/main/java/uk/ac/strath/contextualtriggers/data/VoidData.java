package uk.ac.strath.contextualtriggers.data;

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

}
