package uk.ac.strath.keepfit.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import uk.ac.strath.keepfit.view.KeepFitViewModel;
import uk.ac.strath.keepfit.view.StatisticsFragment;

public class StatisticsCalculator {

    private double averageStepCount;
    private int totalStepCount;
    private double goalCompletion;
    private int currentStreak;
    private int longestStreak;
    private int mostActiveDay;
    private CalendarDay recordsStart;
    private HistoryEntry todaysEntry;

    private StatisticsFragment frag;

    public StatisticsCalculator(StatisticsFragment frag, KeepFitViewModel vm, HistoryEntry todaysEntry) {
        LiveData<List<HistoryEntry>> ld = vm.getAllEntries();
        this.frag = frag;
        this.todaysEntry = todaysEntry;
        ld.observe(frag, new Observer<List<HistoryEntry>>() {
            @Override
            public void onChanged(@Nullable List<HistoryEntry> historyEntries) {
                calculate(historyEntries);
            }
        });
        averageStepCount = 0;
        totalStepCount = 0;
        goalCompletion = 0;
        currentStreak = 0;
        longestStreak = 0;
        mostActiveDay = 0;
        recordsStart = CalendarDay.today();
    }

    private void calculate(@Nullable List<HistoryEntry> data) {
        if (data == null) {
            if (todaysEntry == null) {
                return;
            }
            data = new ArrayList<>();
        }
        Collections.sort(data);
        int nDays = data.size();
        int goalsCompleted = 0;
        int streak = 0;
        int longest = 0;
        CalendarDay lastDate = null;
        int[] dayActivity = new int[8];
        for (int i = 0; i < 8; i++) {
            dayActivity[i] = 0;
        }
        for (HistoryEntry e : data) {
            totalStepCount += e.getStepCount();
            dayActivity[e.getDate().getCalendar().get(Calendar.DAY_OF_WEEK)] += e.getStepCount();
            CalendarDay dayBefore = CalendarDay.from(new Date(e.getDate().getDate().getTime() - 24*60*60*1000));
            if (e.getStepCount() < e.getGoal().getTarget() || !dayBefore.equals(lastDate)) {
                if (streak > longest) {
                    longest = streak;
                }
                if (e.getStepCount() >= e.getGoal().getTarget()) {
                    streak = 1;
                    goalsCompleted++;
                } else {
                    streak = 0;
                }
            } else {
                goalsCompleted++;
                streak++;
            }
            lastDate = e.getDate();
            if (recordsStart == null || e.getDate().isBefore(recordsStart)) {
                recordsStart = e.getDate();
            }
        }
        if (todaysEntry != null) {
            totalStepCount += todaysEntry.getStepCount();
            dayActivity[todaysEntry.getDate().getCalendar().get(Calendar.DAY_OF_WEEK)] += todaysEntry.getStepCount();
            nDays++;
            if (todaysEntry.getStepCount() > todaysEntry.getGoal().getTarget()) {
                goalsCompleted++;
                streak++;
            }
            if (data.isEmpty()) {
                recordsStart = todaysEntry.getDate();
            }
        }
        averageStepCount = totalStepCount * 1.0 / nDays;
        goalCompletion = goalsCompleted * 1.0 / nDays;
        longestStreak = Math.max(longest, streak);
        currentStreak = streak;
        int highestActivity = 0;
        for (int i = 1; i < 8; i++) {
            if (dayActivity[i] > highestActivity) {
                highestActivity = dayActivity[i];
                mostActiveDay = i;
            }
        }
        frag.updateDisplay(this);
    }

    public double getAverageStepCount() {
        return averageStepCount;
    }

    public int getTotalStepCount() {
        return totalStepCount;
    }

    public double getGoalCompletion() {
        return goalCompletion;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public int getLongestStreak() {
        return longestStreak;
    }

    public int getMostActiveDay() {
        return mostActiveDay;
    }

    public CalendarDay getRecordsStart() {
        return recordsStart;
    }

}
