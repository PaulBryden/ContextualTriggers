package uk.ac.strath.contextualtriggers.conditions;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import uk.ac.strath.contextualtriggers.data.VoidData;
import uk.ac.strath.contextualtriggers.managers.IDataManager;

/**
 * A condition that is satisfied if the user has not been notified today.
 */
public class NotNotifiedTodayCondition extends DataCondition<VoidData> {

    public NotNotifiedTodayCondition(IDataManager<VoidData> dataManager) {
        super(dataManager);
    }

    @Override
    public boolean hasStaleData() {
        return false;
    }

    @Override
    public boolean isSatisfied() {
        if (getData() == null) {
            return true;
        }
        LocalDate date = Instant.ofEpochMilli(getData().getTimestamp()).atZone(ZoneId.systemDefault()).toLocalDate();
        return !date.equals(LocalDate.now());
    }

}
