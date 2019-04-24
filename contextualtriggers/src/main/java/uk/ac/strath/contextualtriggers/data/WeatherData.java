package uk.ac.strath.contextualtriggers.data;

import java.lang.reflect.Type;
import java.util.Arrays;

public class WeatherData extends  AbstractData
{
    public float TemperatureCelsius;
    public int Humidity;
    public int[] Conditions;

    public void printData()
    {
        System.out.println(toString());

    }
    public String toString()
    {
        String loggingInfo="WeatherData:\n"+"Time: "+System.currentTimeMillis()+"\n";
        loggingInfo+="Temperature (Celsius):"+TemperatureCelsius+"\n"+"Humidity (%):"+Humidity+"\n";
        for(int i=0;i<Conditions.length;i++)
        {
            loggingInfo+="Condition "+i+": "+Conditions[i]+"\n";
        }
        return loggingInfo;

    }

    @Override
    public boolean equals(Object o){
        if(o instanceof WeatherData){
            return super.equals(o) &&
                    ((WeatherData) o).TemperatureCelsius == (this.TemperatureCelsius) &&
                    ((WeatherData) o).Humidity == (this.Humidity) &&
                    Arrays.equals(((WeatherData) o).Conditions, this.Conditions);
        }
        return false;
    }

    public static Type getType() {
        return WeatherData.class;
    }
}
