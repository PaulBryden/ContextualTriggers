package uk.ac.strath.contextualtriggers.managers;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.WeatherResult;
import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.common.api.ResultCallback;

import uk.ac.strath.contextualtriggers.Logger;
import uk.ac.strath.contextualtriggers.MainApplication;
import uk.ac.strath.contextualtriggers.conditions.DataCondition;
import uk.ac.strath.contextualtriggers.data.WeatherData;

public class WeatherDataManager implements IDataManager<WeatherData>, IDataManagerSource, Runnable
{
    int MY_PERMISSIONS_REQUEST_READ_CONTACTS;
    Logger logger;
    boolean isRunning=false;
    private DataManager<WeatherData> dataManager;
    private static WeatherDataManager singletonWeatherDataManager= null; ;

    private WeatherDataManager()
    {
        dataManager= new DataManager<WeatherData>();
        logger = Logger.getInstance();
        singletonWeatherDataManager=this;
    }

    public static WeatherDataManager getInstance()
    {
        if (singletonWeatherDataManager == null)
            singletonWeatherDataManager = new WeatherDataManager();

        return singletonWeatherDataManager;
    }


    public void register(DataCondition<WeatherData> dataCondition)
    {
        dataManager.register(dataCondition);
    }

    private void sendUpdate(WeatherData data)
    {
        dataManager.sendUpdate(data);
    }

    @RequiresPermission("android.permission.ACCESS_FINE_LOCATION")
    public void run()
    {
        while(isRunning)
        {
            if (ContextCompat.checkSelfPermission(MainApplication.getAppContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED)
            {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainApplication.getAppActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION))
                {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else
                {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(MainApplication.getAppActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                }
            } else
            {
                // Permission has already been granted
                Awareness.SnapshotApi.getWeather(MainApplication.getGoogleAPIClient())
                        .setResultCallback(new ResultCallback<WeatherResult>()
                        {
                            @Override
                            public void onResult(@NonNull WeatherResult weatherResult)
                            {
                                if (!weatherResult.getStatus().isSuccess())
                                {
                                    System.out.println("ERROR");
                                    return;
                                }

                                //parse and display current weather status
                                Weather weather = weatherResult.getWeather();
                                WeatherData data = new WeatherData();
                                data.TemperatureCelsius=weather.getTemperature(Weather.CELSIUS);
                                data.Humidity=weather.getHumidity();
                                data.Conditions=weather.getConditions();
                                data.printData();
                                logger.log(data.toString());
                                sendUpdate(data);
                            }
                        });
            }
            try
            {
                Thread.sleep(10000);
            }catch(Exception e)
            {

            }
        }
    }
    @Override
    public void start()
    {
        if(!isRunning)
        {
            Thread aThread = new Thread(this);
            isRunning = true;
            aThread.start();
        }
    }

}
