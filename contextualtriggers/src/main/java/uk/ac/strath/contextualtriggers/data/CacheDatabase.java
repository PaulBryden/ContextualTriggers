package uk.ac.strath.contextualtriggers.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Database(entities = {DataEntity.class}, version = 0)
public abstract class CacheDatabase extends RoomDatabase{

    public abstract DataDAO DataDao();

    public static volatile CacheDatabase INSTANCE;

    public static int cache_keeptime = 30;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    static CacheDatabase getDatabase(final Context context) {
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
                long oldestTimestamp = cache_keeptime * 1000 * 60 * 60 * 24;
                for(DataEntity d : INSTANCE.DataDao().getAll()){
                    if(d.data.getTimestamp() < oldestTimestamp){
                        INSTANCE.DataDao().delete(d);
                    }
                }
            });
        }
    };


    /* Generic Queries */

    public void insert(DataEntity e){
        executor.submit(() -> {
            DataDao().insert(e);
        });
    }

    public void delete(DataEntity e){
        executor.submit(() -> {
            DataDao().delete(e);
        });
    }

    public void update(DataEntity e){
        executor.submit(() -> {
           DataDao().update(e);
        });
    }

    public Future<List<DataEntity>> getAll(){
        return executor.submit(() -> {
            return DataDao().getAll();
        });

    }

    public void deleteAll(){
        executor.submit(() -> {
            DataDao().deleteAll();
        });
    }
    /*******************/

    /* Queries for interacting with all data of a given type */
    public Future<List<DataEntity>> getAllOfType(String type){
        return executor.submit(() -> {
           return DataDao().getAllOfType(type);
        });

    }

    public void deleteAllOfType(String type){
        executor.submit(() -> {
            DataDao().deleteAllOfType(type);
        });
    }
    /*********************************************************/




}
