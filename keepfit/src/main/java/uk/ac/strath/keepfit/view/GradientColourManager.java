package uk.ac.strath.keepfit.view;


import java.util.HashMap;
import java.util.Map;

import uk.ac.strath.keepfit.R;

public class GradientColourManager {

    static class Range {
        int min;
        int max;
        Range(int min, int max) {
            this.min = min;
            this.max = max;
        }
    }

    static final Map<Integer, Range> RANGES = new HashMap<>();

    static {
        RANGES.put(R.color.gradient1, new Range(0, 39));
        RANGES.put(R.color.gradient2, new Range(40, 59));
        RANGES.put(R.color.gradient3, new Range(60, 79));
        RANGES.put(R.color.gradient4, new Range(80, 99));
        RANGES.put(R.color.gradient5, new Range(100, 124));
        RANGES.put(R.color.gradient6, new Range(125, Integer.MAX_VALUE));
    }

    static int getColour(int percentage) {
        for (Map.Entry<Integer, Range> e : RANGES.entrySet()) {
            if (percentage >= e.getValue().min && percentage <= e.getValue().max) {
                return e.getKey();
            }
        }
        return R.color.black;  // Somethings broken
    }

}
