package uk.ac.strath.contextualtriggers.data;

public class AbstractData implements Data
{
    private long timestamp = System.currentTimeMillis();

    @Override
    public long getTimestamp() {
        return timestamp;
    }
}
