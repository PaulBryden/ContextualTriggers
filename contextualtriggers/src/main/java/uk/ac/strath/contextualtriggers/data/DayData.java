package uk.ac.strath.contextualtriggers.data;

import java.time.LocalDate;

public class DayData
{
    public int steps;
    public int goal;
    public LocalDate date;

    public DayData(int steps, int goal, LocalDate date)
    {
        this.steps = steps;
        this.goal = goal;
        this.date = date;
    }
}
