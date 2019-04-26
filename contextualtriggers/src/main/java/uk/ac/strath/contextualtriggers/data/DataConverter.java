package uk.ac.strath.contextualtriggers.data;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class DataConverter {
    @TypeConverter
    public static Data StringToData(String s) {
        String[] split = s.split(":", 2);

        split[0] = split[0].split(" ", 2)[1];

        System.out.println(split[0]);
        System.out.println(split[1]);

        try {
            Type t = Class.forName(split[0]);
            Gson g = new Gson();
            return g.fromJson(split[1], t);
        } catch (ClassNotFoundException e) {
            System.out.println("illegal Type: " + split[0]);
            return null;
        }

    }

    @TypeConverter
    public static String DataToString(Data d) {
        Type t = d.getClass();
        Gson g = new Gson();
        String s = g.toJson(d, t);
        s = t.toString() + ":" + s;
        return s;
    }
}
