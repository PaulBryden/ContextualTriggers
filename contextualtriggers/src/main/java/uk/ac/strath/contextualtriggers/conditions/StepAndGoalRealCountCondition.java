package uk.ac.strath.contextualtriggers.conditions;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import uk.ac.strath.contextualtriggers.data.DayData;
import uk.ac.strath.contextualtriggers.data.StepAndGoalData;
import uk.ac.strath.contextualtriggers.data.StepData;
import uk.ac.strath.contextualtriggers.managers.ActualStepAndGoalDataManager;
import uk.ac.strath.contextualtriggers.managers.IDataManager;

public class StepAndGoalRealCountCondition extends DataCondition<StepAndGoalData> {

    private int mode;

    public final static int LESS_THAN = 0;
    public final static int GREATER_THAN = 1;

    public StepAndGoalRealCountCondition(int mode, IDataManager<StepAndGoalData> dataManager) {
        super(dataManager);
        this.mode = mode;
    }

    @Override
    public boolean isSatisfied() {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal0 = Calendar.getInstance();
        Date today = cal0.getTime();
        try
        {
            today = formatter.parse(formatter.format(today));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        getData().getDay(today);

        if (getData().getDay(today).steps < 0) { // no step count updates received yet
            return false;
        }
        if (this.mode == LESS_THAN) {
            return getData().getDay(today).steps < getData().getDay(today).goal;
        }
        if (this.mode == GREATER_THAN) {
            return  getData().getDay(today).steps > getData().getDay(today).goal;
        }
        return false;
    }
}
