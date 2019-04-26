package uk.ac.strath.contextualtriggers.data;

import java.lang.reflect.Type;

public interface Data {

    long getTimestamp();

    static Type getType() {
        return Data.class;
    }

}
