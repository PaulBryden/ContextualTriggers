package uk.ac.strath.keepfit.model;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.List;

public class KeepFitRepository {

    private HistoryEntryDao entryDao;

    public KeepFitRepository(Application application) {
        KeepFitRoomDatabase db = KeepFitRoomDatabase.getDatabase(application);
        entryDao = db.entryDao();
    }

    public LiveData<List<HistoryEntry>> getAllEntries() {
        return entryDao.getAllEntries();
    }

    public LiveData<List<HistoryEntry>> getEntries(CalendarDay startDate, CalendarDay endDate) {
        return entryDao.getEntries(HistoryEntry.convertToDateCode(startDate), HistoryEntry.convertToDateCode(endDate));
    }

    public void insert(HistoryEntry entry) {
        new InsertAsyncTask(entryDao).execute(entry);
    }

    public void deleteAllEntries() {
        new DeleteAllAsyncTask(entryDao).execute();
    }

    public void deleteEntriesOlderThan(CalendarDay date) {
        new DeleteOldAsyncTask(entryDao, date).execute();
    }

    private class InsertAsyncTask extends AsyncTask<HistoryEntry, Void, Void> {

        private HistoryEntryDao asyncTaskDao;

        InsertAsyncTask(HistoryEntryDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(HistoryEntry... entries) {
            asyncTaskDao.insert(entries[0]);
            return null;
        }
    }

    private class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {

        private HistoryEntryDao asyncTaskDao;

        DeleteAllAsyncTask(HistoryEntryDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            asyncTaskDao.deleteAll();
            return null;
        }
    }

    private class DeleteOldAsyncTask extends AsyncTask<Void, Void, Void> {

        private HistoryEntryDao asyncTaskDao;
        private CalendarDay cutoff;

        DeleteOldAsyncTask(HistoryEntryDao dao, CalendarDay cutoff) {
            asyncTaskDao = dao;
            this.cutoff = cutoff;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            asyncTaskDao.deleteOlderThan(HistoryEntry.convertToDateCode(cutoff));
            return null;
        }
    }
}
