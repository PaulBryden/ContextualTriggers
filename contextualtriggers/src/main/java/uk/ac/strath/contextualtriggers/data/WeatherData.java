package uk.ac.strath.contextualtriggers.data;

import java.lang.reflect.Type;
import java.util.Arrays;

public class WeatherData extends  AbstractData
{
    private final double temperatureCelsius;
    private final double humidity;
    private final int[] conditions;

    public WeatherData(double temperature, double humidity, int[] conditions) {
        this.temperatureCelsius = temperature;
        this.humidity = humidity;
        this.conditions = conditions;
    }

    public double getTemperatureCelsius() {
        return temperatureCelsius;
    }

    public double getHumidity() {
        return humidity;
    }

    public int[] getConditions() {
        return conditions;
    }

    public String toString()
    {
        String loggingInfo = "WeatherData:\n" + "Time: " + System.currentTimeMillis() + "\n";
        loggingInfo += "Temperature (Celsius):" + temperatureCelsius + "\n"+"Humidity (%):" + humidity + "\n";
        for(int i = 0; i < conditions.length; i++)
        {
            loggingInfo += "Condition " + i + ": " + conditions[i] + "\n";
        }
        return loggingInfo;

    }

    @Override
    public boolean equals(Object o){
        if(o instanceof WeatherData){
            return super.equals(o) &&
                    ((WeatherData) o).temperatureCelsius == (this.temperatureCelsius) &&
                    ((WeatherData) o).humidity == (this.humidity) &&
                    Arrays.equals(((WeatherData) o).conditions, this.conditions);
        }
        return false;
    }

    public static Type getType() {
        return WeatherData.class;
    }
}
