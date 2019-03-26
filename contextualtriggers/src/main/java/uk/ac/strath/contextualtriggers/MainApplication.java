package uk.ac.strath.contextualtriggers;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

import uk.ac.strath.contextualtriggers.actions.NotificationAction;
import uk.ac.strath.contextualtriggers.conditions.AndCondition;
import uk.ac.strath.contextualtriggers.conditions.NotificationHistoryCondition;
import uk.ac.strath.contextualtriggers.conditions.StepCountCondition;
import uk.ac.strath.contextualtriggers.conditions.WeatherSunnyCondition;
import uk.ac.strath.contextualtriggers.data.WeatherData;
import uk.ac.strath.contextualtriggers.managers.StepDataManager;
import uk.ac.strath.contextualtriggers.managers.WeatherDataManager;
import uk.ac.strath.contextualtriggers.triggers.Trigger;

public class MainApplication extends AppCompatActivity {
    private static GoogleApiClient mGoogleApiClient;
    private static AppCompatActivity mAppActivity;
    private static Context context;
    WeatherDataManager weatherData;
    Logger logger;
    Trigger sunnyOotsideTrigger;

    public static Context getAppContext() {
        return MainApplication.context;
    }

    public static AppCompatActivity getAppActivity() {
        return mAppActivity;
    }

    public static GoogleApiClient getGoogleAPIClient() {
        return mGoogleApiClient;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.context = getApplicationContext();
        setContentView(R.layout.scrollable_textview);
        TextView textView = (TextView) findViewById(R.id.text_view);
        textView.setMovementMethod(new ScrollingMovementMethod());
        logger = Logger.getInstance();
        logger.setLogger(textView);

        //noinspection MissingPermission
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Awareness.API)
                .build();
        mGoogleApiClient.connect();
        mAppActivity = this;
        sunnyOotsideTrigger = sunnyOotsideTrigger();

    }

    private Trigger sunnyOotsideTrigger() {
        ArrayList<Condition> conditionList = new ArrayList<>();
        /*Setup Conditions*/
        StepCountCondition stepCondition = new StepCountCondition(StepCountCondition.LESS_THAN, 10000);
        WeatherData targetWeather = new WeatherData();
        targetWeather.TemperatureCelsius = 14;
        WeatherSunnyCondition sunnyCondition = new WeatherSunnyCondition(targetWeather);
        NotificationHistoryCondition notifyCondition = new NotificationHistoryCondition(30);
        /*Link Conditions to Data Manager*/
        StepDataManager stepDataManager = StepDataManager.getInstance();
        WeatherDataManager weatherDataManager = WeatherDataManager.getInstance();
        stepDataManager.register(stepCondition);
        weatherDataManager.register(sunnyCondition);
        conditionList.add(stepCondition);
        conditionList.add(sunnyCondition);
        conditionList.add(notifyCondition);
        AndCondition condition = new AndCondition(conditionList);
        NotificationAction action = new NotificationAction("Go for a walk ya lazy. It's even sunny ootside!");
        action.attachCondition(notifyCondition);
        Trigger trigger = new Trigger.Builder().setCondition(condition).setAction(action).build();
        condition.attachTrigger(trigger);
        stepDataManager.start();
        weatherDataManager.start();
        return trigger;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

}
