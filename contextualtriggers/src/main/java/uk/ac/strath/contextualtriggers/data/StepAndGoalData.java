package uk.ac.strath.contextualtriggers.data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StepAndGoalData extends  AbstractData
{
    private HashMap<Date,DayData> history;
    public StepAndGoalData()
    {
        history = new HashMap<>();
        /*
        POPULATE STRUCT HERE FROM HISTORY
         */

        DayData tempDay1= new DayData(500,5000, getDateWithOffset(0));
        DayData tempDay2= new DayData(500,5000, getDateWithOffset(-1));
        DayData tempDay3= new DayData(500,5000, getDateWithOffset(-2));
        DayData tempDay4= new DayData(500,5000, getDateWithOffset(-3));
        updateDay(tempDay1);
        updateDay(tempDay2);
        updateDay(tempDay3);
        updateDay(tempDay4);
    }

    private Date getDateWithOffset(int offset)
    {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, offset); // number represents number of days
        Date now = cal.getTime();

        try
        {
            now = formatter.parse(formatter.format(now));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return now;
    }

    public Map<Date,DayData> getHistory()
    {
        return history;
    }

    public void updateDay(DayData day)
    {
        try
        {
            history.get(day.date).goal = day.goal;
            history.get(day.date).steps = day.steps;
        }
        catch(NullPointerException e)
        {
            history.put(day.date,day);
        }
    }

    public DayData getDay(Date day)
    {
        return history.get(day);
    }
}
