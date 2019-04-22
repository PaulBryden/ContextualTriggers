package uk.ac.strath.contextualtriggers.conditions;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import uk.ac.strath.contextualtriggers.data.VoidData;
import uk.ac.strath.contextualtriggers.managers.IDataManager;

/**
 * Basic condition that checks if a notification has been sent recently.
 * Condition is satisfied if time elapsed since last condition is more than
 * specified amount.
 */
public class NotNotifiedTodayCondition extends DataCondition<VoidData>
{

    private Date lastNotificationSent;

    public NotNotifiedTodayCondition(IDataManager dataManager)
    {
        super(dataManager);
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date yesterday = cal.getTime();
        try
        {
            yesterday = formatter.parse(formatter.format(yesterday));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        lastNotificationSent=yesterday;
    }

    @Override
    public void notifyUpdate(VoidData data)
    {
        // Override since an update always means condition isn't satisfied,
        // so no need to notify the Trigger of the change.
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
        lastNotificationSent = today;
        super.notifyUpdate(data);
    }

    @Override
    public boolean isSatisfied()
    {
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
        return today.after(lastNotificationSent);
    }
}
