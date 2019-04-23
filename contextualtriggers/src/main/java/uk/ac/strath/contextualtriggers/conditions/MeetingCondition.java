package uk.ac.strath.contextualtriggers.conditions;

import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;
import java.util.Date;

import uk.ac.strath.contextualtriggers.data.CalendarData;
import uk.ac.strath.contextualtriggers.data.EventData;
import uk.ac.strath.contextualtriggers.managers.IDataManager;

public class MeetingCondition extends DataCondition<CalendarData> {

    private String name;

    public MeetingCondition(IDataManager<CalendarData> dataManager) {
        super(dataManager);
    }

    @Override
    public boolean isSatisfied() {
        Calendar c = Calendar.getInstance();
        Date t = c.getTime();
        c.add(Calendar.HOUR, 2);
        Date t2 = c.getTime();
        for (EventData event : getData().getEventsBetween(t, t2)) {
            if (StringUtils.containsIgnoreCase(event.name, "meet")) {
                name = event.name;
                return true;
            }
        }
        return false;
    }

    public String getName() {
        return name;
    }

}
