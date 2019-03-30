package uk.ac.strath.contextualtriggers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.common.api.GoogleApiClient;
import uk.ac.strath.contextualtriggers.managers.WeatherDataManager;
import uk.ac.strath.contextualtriggers.triggers.ButItsSunnyOutsideTrigger;
import uk.ac.strath.contextualtriggers.triggers.Trigger;

public class MainApplication extends AppCompatActivity
{
    private final Boolean serviceMode = false;


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


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        MainApplication.context = getApplicationContext();
        mAppActivity=this;
        if(serviceMode){
            emptyActivity();
        } else {
            logActivity();
        }

    }

    @Override
    protected void onResume()
    {
        super.onResume();

    }


    private void emptyActivity(){
        this.setTheme(R.style.Theme_Transparent);
        Intent i = new Intent(this, ContextualTriggersService.class);
        startService(i);
        this.finish();
    }

    private void logActivity(){
        setContentView(R.layout.scrollable_textview);
        TextView textView = (TextView) findViewById(R.id.text_view);
        textView.setMovementMethod(new ScrollingMovementMethod());
        logger = Logger.getInstance();
        logger.setLogger(textView);
        ButItsSunnyOutsideTrigger sunnyOotsideTrigger = new ButItsSunnyOutsideTrigger();
    }
}
