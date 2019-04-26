package uk.ac.strath.contextualtriggers.data;

import android.support.annotation.Nullable;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class StepAndGoalData extends AbstractData {
    private HashMap<String, DayData> history;

    public StepAndGoalData(HashMap<String, DayData> data) {
        this.history = data;
    }

    public StepAndGoalData() {
        this(new HashMap<>());
    }

    public Map<String, DayData> getHistory() {
        return history;
    }

    public void updateDay(DayData day) {
        history.put(day.date.toString(), day);
    }

    @Nullable
    public DayData getDay(LocalDate day) {
        return history.get(day.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof StepAndGoalData) {
            boolean b = super.equals(o);
            b &= ((StepAndGoalData) o).history.size() == this.history.size();
            for (String s : ((StepAndGoalData) o).history.keySet()) {
                b &= ((StepAndGoalData) o).history.get(s).equals(this.history.get(s));
            }
            return b;
        }
        return false;
    }

    public static Type getType() {
        return StepAndGoalData.class;
    }
}
