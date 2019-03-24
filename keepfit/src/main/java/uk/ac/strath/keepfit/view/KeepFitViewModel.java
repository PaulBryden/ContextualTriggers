package uk.ac.strath.keepfit.view;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.Date;
import java.util.List;

import uk.ac.strath.keepfit.model.HistoryEntry;
import uk.ac.strath.keepfit.model.KeepFitRepository;
import uk.ac.strath.keepfit.model.SharedPreferencesManager;

public class KeepFitViewModel extends AndroidViewModel {

    private SharedPreferencesManager spm;
    private KeepFitRepository repository;

    public KeepFitViewModel(@NonNull Application application) {
        super(application);
        repository = new KeepFitRepository(application);
        spm = new SharedPreferencesManager(application);
    }

    public LiveData<List<HistoryEntry>> getAllEntries() {
        return repository.getAllEntries();
    }

    public LiveData<List<HistoryEntry>> getEntries(CalendarDay startDate, CalendarDay endDate) {
        return repository.getEntries(startDate, endDate);
    }

    // TODO - need to periodically delete old data at some point?

    public void insert(HistoryEntry entry) {
        int duration = spm.getHistoryDuration();
        long ms = CalendarDay.today().getDate().getTime() - entry.getDate().getDate().getTime();
        if (duration < 0 || (ms / (24*60*60*1000) <= duration)) {
            repository.insert(entry);
        }
    }

    public void deleteAllEntries() {
        repository.deleteAllEntries();
    }

    public void deleteOldEntries() {
        int duration = spm.getHistoryDuration();
        CalendarDay cutoff = CalendarDay.from(new Date(CalendarDay.today().getDate().getTime() - (24L * 60*60*1000*duration)));
        repository.deleteEntriesOlderThan(cutoff);
    }
}
