package uk.ac.strath.keepfit.model;

import android.arch.persistence.room.ColumnInfo;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import uk.co.daviddunphy.keepfit.R;

public class Goal implements Comparable<Goal> {

    @ColumnInfo(name = "goal_target")
    private int target;

    @ColumnInfo(name = "goal_name")
    private String name;

    public Goal(String name, int target) {
        this.target = target;
        this.name = name;
    }

    public static List<Goal> getDefaultGoals(Context context) {
        List<Goal> goals = new ArrayList<>();
        String[] names = context.getResources().getStringArray(R.array.list_default_goal_names);
        String[] vals = context.getResources().getStringArray(R.array.list_default_goal_targets);
        for (int i = 0; i < names.length; i++) {
            goals.add(new Goal(names[i], Integer.parseInt(vals[i])));
        }
        return goals;
    }

    public String getName() {
        return name;
    }

    public int getTarget() {
        return target;
    }

    @Override
    public int compareTo(Goal o) {
        return ((Integer) this.target).compareTo(o.target);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTarget(int target) {
        this.target = target;
    }
}
