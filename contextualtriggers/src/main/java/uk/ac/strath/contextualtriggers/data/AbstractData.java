package uk.ac.strath.contextualtriggers.data;

public class AbstractData implements Data
{
    private final long timestamp;

    public AbstractData() {
        this.timestamp = System.currentTimeMillis();
    }

    public AbstractData(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }
}
