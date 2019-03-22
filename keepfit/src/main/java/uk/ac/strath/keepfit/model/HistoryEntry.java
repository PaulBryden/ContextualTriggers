package uk.ac.strath.keepfit.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.prolificinteractive.materialcalendarview.CalendarDay;


@Entity(tableName = "history_entries")
public class HistoryEntry implements Comparable<HistoryEntry> {

    @PrimaryKey
    private int dateCode;

    @Ignore
    private CalendarDay date;

    @Embedded
    private Goal goal;

    private int stepCount;

    public HistoryEntry(int dateCode, Goal goal, int stepCount) {
        this.date = convertFromDateCode(dateCode);
        this.dateCode = dateCode;
        this.goal = goal;
        this.stepCount = stepCount;
    }

    public HistoryEntry(CalendarDay date, Goal goal, int stepCount) {
        this.date = date;
        this.dateCode = convertToDateCode(date);
        this.goal = goal;
        this.stepCount = stepCount;
    }

    public Goal getGoal() {
        return goal;
    }

    public int getStepCount() {
        return stepCount;
    }

    public int getDateCode() {
        return dateCode;
    }

    public CalendarDay getDate() {
        return date;
    }

    public int getPercentage() {
        return (int) Math.round(100.0 * stepCount / goal.getTarget());
    }

    public static final int convertToDateCode(CalendarDay date) {
        return date.getYear() * 10000 + date.getMonth() * 100 + date.getDay();
    }

    public static final CalendarDay convertFromDateCode(int dateCode) {
        return CalendarDay.from(dateCode / 10000, (dateCode % 10000) / 100, dateCode % 100);
    }

    @Override
    public int compareTo(HistoryEntry o) {
        if (dateCode < o.dateCode) {
            return -1;
        }
        if (dateCode > o.dateCode) {
            return 1;
        }
        return 0;
    }
}
