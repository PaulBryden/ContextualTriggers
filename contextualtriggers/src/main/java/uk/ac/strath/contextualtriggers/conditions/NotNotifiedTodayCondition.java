package uk.ac.strath.contextualtriggers.conditions;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import uk.ac.strath.contextualtriggers.data.PlacesData;
import uk.ac.strath.contextualtriggers.data.VoidData;
import uk.ac.strath.contextualtriggers.managers.IDataManager;

/**
 * Basic condition that checks if a notification has been sent recently.
 * Condition is satisfied if time elapsed since last condition is more than
 * specified amount.
 */
public class NotNotifiedTodayCondition extends DataCondition<VoidData>
{
    LocalDate date;
    public NotNotifiedTodayCondition(IDataManager<VoidData> dataManager)
    {
        super(dataManager);
        date=LocalDate.ofEpochDay(0);
    }

    @Override
    public boolean hasStaleData() {
        return false;
    }

    @Override
    public void notifyUpdate(VoidData data)
    {
        date = LocalDate.now();
        super.notifyUpdate(data);
    }

    @Override
    public boolean isSatisfied()
    {
        return !date.equals(LocalDate.now());
    }
}
