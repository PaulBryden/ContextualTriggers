package uk.ac.strath.contextualtriggers.data;

import java.util.Date;

public class DayData
{
    public DayData(int steps, int goal, Date date)
    {
        this.steps=steps;
        this.goal=goal;
        this.date=date;
    }
    public int steps;
    public int goal;
    public Date date;
}
