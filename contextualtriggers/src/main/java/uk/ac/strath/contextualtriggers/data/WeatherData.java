package uk.ac.strath.contextualtriggers.data;

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
}
