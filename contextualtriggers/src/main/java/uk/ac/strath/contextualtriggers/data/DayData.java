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

    @Override
    public boolean equals(Object o){
        if(o instanceof DayData){
            return  ((DayData) o).steps == (this.steps) &&
                    ((DayData) o).goal == (this.goal) &&
                    ((DayData) o).date.equals(this.date);
        }
        return false;
    }

    @Override
    public String toString(){
        return String.format("DayData : {steps : %s, goal : %s, date: %s}", steps, goal, date);
    }
}
