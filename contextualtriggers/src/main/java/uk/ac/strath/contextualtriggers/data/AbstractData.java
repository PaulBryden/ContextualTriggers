package uk.ac.strath.contextualtriggers.data;

public class AbstractData implements Data
{
    private long timestamp = System.currentTimeMillis();

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof AbstractData){
            return ((AbstractData) o).timestamp == this.timestamp;
        }
        return false;
    }
}
