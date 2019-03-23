package uk.ac.strath.contextualtriggers;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.common.api.GoogleApiClient;
import uk.ac.strath.contextualtriggers.managers.WeatherDataManager;

public class MainApplication extends AppCompatActivity
{
    private static GoogleApiClient mGoogleApiClient;
    private static AppCompatActivity mAppActivity;
    private static Context context;
    WeatherDataManager weatherData;
    Logger logger;

    public static Context getAppContext() {
        return MainApplication.context;
    }

    public static AppCompatActivity getAppActivity()
    {
        return mAppActivity;
    }

    public static GoogleApiClient getGoogleAPIClient()
    {
        return mGoogleApiClient;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        MainApplication.context = getApplicationContext();
        setContentView(R.layout.scrollable_textview);
        TextView textView = (TextView) findViewById(R.id.text_view);
        textView.setMovementMethod(new ScrollingMovementMethod());
        logger = new Logger(textView);

        //noinspection MissingPermission
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Awareness.API)
                .build();
        mGoogleApiClient.connect();
        mAppActivity=this;
        weatherData = new WeatherDataManager();
        weatherData.start();

    }

    @Override
    protected void onResume()
    {
        super.onResume();

    }

}
