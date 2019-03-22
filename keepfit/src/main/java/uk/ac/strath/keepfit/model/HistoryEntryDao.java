package uk.ac.strath.keepfit.model;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface HistoryEntryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(HistoryEntry entry);

    @Query("DELETE FROM history_entries")
    void deleteAll();

    @Query("DELETE FROM history_entries WHERE dateCode < :cutoff")
    void deleteOlderThan(int cutoff);

    @Query("SELECT * FROM history_entries")
    LiveData<List<HistoryEntry>> getAllEntries();

    @Query("SELECT * FROM history_entries WHERE dateCode >= :startDateCode AND dateCode <= :endDateCode ORDER BY dateCode ASC")
    LiveData<List<HistoryEntry>> getEntries(int startDateCode, int endDateCode);
}
