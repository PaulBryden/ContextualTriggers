package uk.ac.strath.contextualtriggers.data;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "data_cache")
public class DataEntity {

    public DataEntity() {

    }

    public DataEntity(Data d) {
        type = d.getClass().toString();
        data = d;
    }

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo
    public String type;
    public Data data;

}
