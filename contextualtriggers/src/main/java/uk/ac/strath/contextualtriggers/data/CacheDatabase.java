package uk.ac.strath.contextualtriggers.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Database(entities = {DataEntity.class}, version = 1, exportSchema = false)
@TypeConverters({DataConverter.class})
public abstract class CacheDatabase extends RoomDatabase {

    public abstract DataDAO DataDao();

    private static volatile CacheDatabase INSTANCE;

    public static int cache_keeptime = 30;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public static CacheDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CacheDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CacheDatabase.class, "cache_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(cb)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback cb = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            // If you want to keep the data through app restarts,
            // comment out the following line.
            Executors.newSingleThreadExecutor().execute(() -> {
                INSTANCE.deleteAll();
                long oldestTimestamp = System.currentTimeMillis() - (cache_keeptime * 1000 * 60 * 60 * 24);
                try {
                    List<DataEntity> l = INSTANCE.getAll().get();
                    for (DataEntity d : l) {
                        if (d.data.getTimestamp() < oldestTimestamp) {
                            INSTANCE.delete(d);
                        }
                    }
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            });
        }
    };


    /* Generic Queries */

    public void insert(DataEntity e) {
        executor.submit(() -> {
            DataDao().insert(e);
        });
    }

    public void insert(Data d) {
        executor.submit(() -> {
            DataEntity e = new DataEntity(d);
            DataDao().insert(e);
        });
    }

    public void delete(DataEntity e) {
        executor.submit(() -> {
            DataDao().delete(e);
        });
    }

    public void update(DataEntity e) {
        executor.submit(() -> {
            DataDao().update(e);
        });
    }

    public Future<List<DataEntity>> getAll() {
        return executor.submit(() -> {
            return DataDao().getAll();
        });

    }

    public void deleteAll() {
        executor.submit(() -> {
            DataDao().deleteAll();
        });
    }

    /*******************/

    /* Queries for interacting with all data of a given type */
    public Future<List<DataEntity>> getAllOfType(String type) {
        return executor.submit(() -> {
            return DataDao().getAllOfType(type);
        });

    }

    public void deleteAllOfType(String type) {
        executor.submit(() -> {
            DataDao().deleteAllOfType(type);
        });
    }

    /*********************************************************/

    public Future<DataEntity> getLatestOfType(String type) {
        return executor.submit(() -> {
            List<DataEntity> l = DataDao().getAllOfType(type);
            if (l.size() == 0) {
                return null;
            }
            DataEntity max = l.get(0);
            for (int i = 1; i < l.size(); i++) {
                if (l.get(i).data.getTimestamp() > max.data.getTimestamp()) {
                    max = l.get(i);
                }
            }
            return max;
        });
    }


}
