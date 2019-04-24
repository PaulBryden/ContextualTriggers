package uk.ac.strath.contextualtriggers.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface DataDAO {

    /* Generic Queries */
    @Insert
    void insert(DataEntity e);

    @Delete
    void delete(DataEntity e);

    @Query("DELETE FROM data_cache")
    void deleteAll();
    /*******************/

    /* Queries for interacting with all data of a given type */
    @Query("SELECT * FROM data_cache WHERE type = :type")
    List<DataEntity> getAllOfType(String type);

    @Query("DELETE FROM data_cache WHERE type = :type")
    void deleteAllOfType(String type);
    /*********************************************************/



}
