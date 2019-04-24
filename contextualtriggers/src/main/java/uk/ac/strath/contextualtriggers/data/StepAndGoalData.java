package uk.ac.strath.contextualtriggers.data;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class StepAndGoalData extends  AbstractData
{
    private HashMap<String, DayData> history;
    public StepAndGoalData()
    {
        history = new HashMap<>();
        /*
        POPULATE STRUCT HERE FROM HISTORY
         */
        setup();


    }

    public void setup(){
        DayData tempDay1= new DayData(500,5000, LocalDate.now());
        DayData tempDay2= new DayData(500,5000, LocalDate.now().minusDays(1));
        DayData tempDay3= new DayData(500,5000, LocalDate.now().minusDays(2));
        DayData tempDay4= new DayData(500,5000, LocalDate.now().minusDays(3));
        updateDay(tempDay1);
        updateDay(tempDay2);
        updateDay(tempDay3);
        updateDay(tempDay4);
    }

    public Map<String, DayData> getHistory()
    {
        return history;
    }

    public void updateDay(DayData day)
    {
        history.put(day.date.toString(),day);
    }

    public DayData getDay(LocalDate day)
    {
        return history.get(day.toString());
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof StepAndGoalData){
            boolean b = super.equals(o);
            b &= ((StepAndGoalData) o).history.size() == this.history.size();
            for(String s : ((StepAndGoalData) o).history.keySet()){
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
