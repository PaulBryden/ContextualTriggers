package uk.ac.strath.keepfit.model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {HistoryEntry.class}, version = 4, exportSchema = false)
public abstract class KeepFitRoomDatabase extends RoomDatabase {
    public abstract HistoryEntryDao entryDao();

    private static volatile KeepFitRoomDatabase INSTANCE;

    static KeepFitRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (KeepFitRoomDatabase.class) {
                if (INSTANCE == null) {
                    // TODO - fallbackToDestructiveMigration is temporary
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            KeepFitRoomDatabase.class, "keep_fit_database").fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}
