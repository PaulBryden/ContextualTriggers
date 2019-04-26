package uk.ac.strath.contextualtriggers.data;

import java.lang.reflect.Type;

public abstract class AbstractData implements Data {
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

    @Override
    public boolean equals(Object o) {
        if (o instanceof AbstractData) {
            return ((AbstractData) o).timestamp == this.timestamp;
        }
        return false;
    }

    public static Type getType() {
        return AbstractData.class;
    }

}
