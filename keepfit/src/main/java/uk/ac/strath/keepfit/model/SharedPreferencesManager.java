package uk.ac.strath.keepfit.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import uk.co.daviddunphy.keepfit.R;

public class SharedPreferencesManager {


    public static final String PREFS_NAME = "KeepFitPrefs";

    private static final String GOAL_SET_KEY = "goal_set";
    private static final String AUTO_STEP_COUNT_KEY = "step_count";
    private static final String MAN_STEP_COUNT_KEY = "manual_step_count";
    private static final String CURRENT_GOAL_KEY = "current_goal";
    private static final String ACTIVE_DATE = "active_date";
    private static final String ACTIVE_FRAGMENT = "active_fragment";
    private static final String STEP_COUNT_OFFSET = "step_count_offset";

    private SharedPreferences preferences;
    private Context context;


    public SharedPreferencesManager(@Nullable Context context) {
        this.context = context;
        if (context != null) {
            preferences = PreferenceManager.getDefaultSharedPreferences(context);
        } else {
            preferences = null;
        }
    }

    private static List<Goal> goalsFromStringSet(Set<String> strings) {
        List<Goal> goals = new ArrayList<>();
        for (String s : strings) {
            String[] split = s.split("\n");
            String n = split[0];
            int t = Integer.parseInt(split[1]);
            goals.add(new Goal(n, t));
        }
        Collections.sort(goals);
        return goals;
    }

    private static Set<String> goalsToStringSet(List<Goal> goals) {
        Set<String> strings = new HashSet<>();
        for (Goal g : goals) {
            strings.add(g.getName() + "\n" + Integer.toString(g.getTarget()));
        }
        return strings;
    }

    public List<Goal> loadGoals() {
        if (preferences != null) {
            Set<String> goalSet = preferences.getStringSet(GOAL_SET_KEY, null);
            if (goalSet != null) {
                return goalsFromStringSet(goalSet);
            }
        }
        return Goal.getDefaultGoals(context);
    }

    public Goal currentGoal(List<Goal> goals) {
        if (preferences != null) {
            String cg = preferences.getString(CURRENT_GOAL_KEY, "");
            for (Goal g : goals) {
                if (g.getName().equals(cg)) {
                    return g;
                }
            }
        }
        if (goals.size() < 1) {
            return new Goal(context.getString(R.string.default_goal_name), 10000);
        }
        return goals.get(0);
    }

    public Goal currentGoal() {
        return this.currentGoal(this.loadGoals());
    }

    public int loadStepCount() {
        if (preferences == null) {
            return 0;
        }
        return preferences.getInt(MAN_STEP_COUNT_KEY, 0) + preferences.getInt(AUTO_STEP_COUNT_KEY, 0) - getStepCountOffset();
    }

    public CalendarDay loadCurrentDate() {
        if (preferences == null) {
            return CalendarDay.today();
        }
        long ms = preferences.getLong(ACTIVE_DATE, Long.MIN_VALUE);
        if (ms != Long.MIN_VALUE) {
            return CalendarDay.from(new Date(ms));
        }
        return CalendarDay.today();
    }

    public int getHistoryDuration() {
        if (preferences == null) {
            return -1;
        }
        String s = preferences.getString(getStringRes(R.string.pref_key_history_length), "-1");
        return Integer.parseInt(s);
    }

    public int getStepCountOffset() {
        if (preferences == null) {
            return 0;
        }
        return preferences.getInt(STEP_COUNT_OFFSET, 0);
    }

    public boolean getDeleteHistory() {
        if (preferences == null) {
            return false;
        }
        return preferences.getBoolean(getStringRes(R.string.pref_key_clear_history), false);
    }

    public boolean isStepCounterEnabled() {
        if (preferences == null) {
            return false;
        }
        return preferences.getBoolean(getStringRes(R.string.pref_key_step_counter_enabled), false);
    }

    public int getActiveFragmentId() {
        if (preferences == null) {
            return -1;
        }
        return preferences.getInt(ACTIVE_FRAGMENT, -1);
    }

    public boolean isGoalEditingEnabled() {
        if (preferences == null) {
            return true;
        }
        return preferences.getBoolean(getStringRes(R.string.pref_key_goal_editing_enabled), true);
    }

    public boolean areNotificationsTurnedOn() {
        if (preferences == null) {
            return true;
        }
        return preferences.getBoolean(getStringRes(R.string.pref_key_notifications_enabled), true);
    }

    public void saveAutoStepCount(int count) {
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(AUTO_STEP_COUNT_KEY, count);
            editor.apply();
        }
    }

    public void addManualStepCount(int count) {
        if (preferences != null) {
            int total = preferences.getInt(MAN_STEP_COUNT_KEY, 0) + count;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(MAN_STEP_COUNT_KEY, total);
            editor.apply();
        }
    }

    public void resetStepCount() {
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(STEP_COUNT_OFFSET, preferences.getInt(AUTO_STEP_COUNT_KEY, 0));
            editor.putInt(AUTO_STEP_COUNT_KEY, 0);
            editor.putInt(MAN_STEP_COUNT_KEY, 0);
            editor.apply();
        }
    }

    public void saveCurrentGoal(Goal goal) {
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(CURRENT_GOAL_KEY, goal.getName());
            editor.apply();
        }
    }

    public void saveGoals(List<Goal> goals) {
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putStringSet(GOAL_SET_KEY, goalsToStringSet(goals));
            editor.apply();
        }
    }

    public void saveCurrentDate(CalendarDay date) {
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putLong(ACTIVE_DATE, date.getDate().getTime());
            editor.apply();
        }
    }

    public void saveActiveFragmentId(int id) {
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(ACTIVE_FRAGMENT, id);
            editor.apply();
        }
    }

    public void setStepCounterEnabled(boolean enabled) {
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(getStringRes(R.string.pref_key_step_counter_enabled), enabled);
            editor.apply();
        }
    }

    public void setDeleteHistory(boolean delete) {
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(getStringRes(R.string.pref_key_clear_history), delete);
            editor.apply();
        }
    }

    private String getStringRes(int resId) {
        return context.getResources().getString(resId);
    }

}
