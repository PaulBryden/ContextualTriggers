package uk.ac.strath.contextualtriggers;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class MainApplication extends AppCompatActivity {
    private final Boolean serviceMode = true;
    private static AppCompatActivity mAppActivity;
    private static Context context;
    private Intent i;
    Logger logger;
    private StepIntentReceiver receiver;
    private GoalIntentReceiver receiver2;

    // Trigger sunnyOotsideTrigger;
    public static Context getAppContext() {
        return MainApplication.context;
    }

    public static AppCompatActivity getAppActivity() {
        return mAppActivity;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        mAppActivity=this;
        if(serviceMode){
            emptyActivity();
        } else {
            logActivity();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.context = getApplicationContext();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @TargetApi(26)
    private void emptyActivity(){
        this.setTheme(R.style.Theme_Transparent);
        i = new Intent(this, ContextualTriggersService.class);
        startForegroundService(i);
        configureReceiver();

    }

    private void logActivity(){
        setContentView(R.layout.scrollable_textview);
        TextView textView = findViewById(R.id.text_view);
        textView.setMovementMethod(new ScrollingMovementMethod());
        logger = Logger.getInstance();
        logger.setLogger(textView);
        Intent i = new Intent(this, ContextualTriggersService.class);
    }


    private void configureReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("uk.ac.strath.contextualtriggers.step");
        receiver = new StepIntentReceiver();
        registerReceiver(receiver, filter);
        IntentFilter filter2 = new IntentFilter();
        filter.addAction("uk.ac.strath.contextualtriggers.goal");
        receiver2 = new GoalIntentReceiver();
        registerReceiver(receiver2, filter);
    }
}
