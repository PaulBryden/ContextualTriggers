package uk.ac.strath.keepfit.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.daviddunphy.keepfit.view.KeepFitViewModel;

public abstract class HistoryEntryObserver {

    private Fragment owner;
    private KeepFitViewModel viewModel;
    private Map<Integer, HistoryEntry> historyEntries;
    private boolean requested;

    public HistoryEntryObserver(Fragment owner) {
        this.owner = owner;
        this.historyEntries = new HashMap<>();
        this.requested = false;
        viewModel = ViewModelProviders.of(owner).get(KeepFitViewModel.class);
        // Add today's entry
        SharedPreferencesManager spm = new SharedPreferencesManager(owner.getActivity());
        Goal currentGoal = spm.currentGoal();
        int stepCount = spm.loadStepCount();
        HistoryEntry entry = new HistoryEntry(CalendarDay.today(), currentGoal, stepCount);
        historyEntries.put(entry.getDateCode(), entry);
    }

    public void requestEntries() {
        if (!requested) {
            LiveData<List<HistoryEntry>> ld = viewModel.getAllEntries();
            ld.observe(owner, new Observer<List<HistoryEntry>>() {
                @Override
                public void onChanged(@Nullable List<HistoryEntry> entries) {
                    if (entries != null) {
                        updateEntries(entries);
                    }
                }
            });
            requested = true;
        }
    }

    private void updateEntries(@NonNull List<HistoryEntry> entries) {
        for (HistoryEntry entry : entries) {
            historyEntries.put(entry.getDateCode(), entry);
        }
        update();
    }

    public Map<Integer, HistoryEntry> getEntries() {
        return historyEntries;
    }

    public HistoryEntry getEntry(CalendarDay day) {
        return historyEntries.get(HistoryEntry.convertToDateCode(day));
    }

    public abstract void update();

}
