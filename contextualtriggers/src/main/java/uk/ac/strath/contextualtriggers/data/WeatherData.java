package uk.ac.strath.contextualtriggers.data;

public class WeatherData extends  AbstractData
{
    private final float temperatureCelsius;
    private final int humidity;
    private final int[] conditions;

    public WeatherData(float temperature, int humidity, int[] conditions) {
        this.temperatureCelsius = temperature;
        this.humidity = humidity;
        this.conditions = conditions;
    }

    public float getTemperatureCelsius() {
        return temperatureCelsius;
    }

    public int getHumidity() {
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
}
