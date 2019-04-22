package uk.ac.strath.contextualtriggers.conditions;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import uk.ac.strath.contextualtriggers.data.StepAndGoalData;
import uk.ac.strath.contextualtriggers.data.StepData;
import uk.ac.strath.contextualtriggers.managers.IDataManager;

public class HistoricStepsDaysUnmetCondition extends DataCondition<StepAndGoalData> {

    private int daysMet;

    public HistoricStepsDaysUnmetCondition(int days, IDataManager<StepAndGoalData> dataManager) {
        super(dataManager);
        daysMet=days;
    }
    private Date getDay(int offset)
    {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, offset);
        Date day = cal.getTime();
        try
        {
            day = formatter.parse(formatter.format(day));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return day;
    }



    @Override
    public boolean isSatisfied()
    {
        boolean state=false;
        for(int i=0;i<daysMet;i++)
        {
            if(getData().getDay(getDay(0)).steps<(getData().getDay(getDay(0)).goal))
            {
                state=true;
            }
        }
        return state;
    }
}
