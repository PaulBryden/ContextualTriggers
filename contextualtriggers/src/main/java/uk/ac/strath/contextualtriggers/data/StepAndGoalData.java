package uk.ac.strath.contextualtriggers.data;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class StepAndGoalData extends  AbstractData
{
    private HashMap<LocalDate, DayData> history;
    public StepAndGoalData()
    {
        history = new HashMap<>();
        /*
        POPULATE STRUCT HERE FROM HISTORY
         */

        DayData tempDay1= new DayData(500,5000, LocalDate.now());
        DayData tempDay2= new DayData(500,5000, LocalDate.now().minusDays(1));
        DayData tempDay3= new DayData(500,5000, LocalDate.now().minusDays(2));
        DayData tempDay4= new DayData(500,5000, LocalDate.now().minusDays(3));
        updateDay(tempDay1);
        updateDay(tempDay2);
        updateDay(tempDay3);
        updateDay(tempDay4);
    }

    public Map<LocalDate, DayData> getHistory()
    {
        return history;
    }

    public void updateDay(DayData day)
    {
        history.put(day.date,day);
    }

    public DayData getDay(LocalDate day)
    {
        return history.get(day);
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof StepAndGoalData){
            return ((StepAndGoalData) o).history.equals(this.history) && super.equals(o);
        }
        return false;
    }
}
