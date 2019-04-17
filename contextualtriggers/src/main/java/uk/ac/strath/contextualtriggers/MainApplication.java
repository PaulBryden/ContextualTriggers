package uk.ac.strath.contextualtriggers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class MainApplication extends AppCompatActivity {
    private final Boolean serviceMode = true;
    private static AppCompatActivity mAppActivity;
    private static Context context;
    Logger logger;

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

    private void emptyActivity(){
        this.setTheme(R.style.Theme_Transparent);
        Intent i = new Intent(this, ContextualTriggersService.class);
        startService(i);
        //this.finish(); This causes destructor to be called on the instance of the contextualtriggersservice class
        this.
    }

    private void logActivity(){
        setContentView(R.layout.scrollable_textview);
        TextView textView = findViewById(R.id.text_view);
        textView.setMovementMethod(new ScrollingMovementMethod());
        logger = Logger.getInstance();
        logger.setLogger(textView);
        Intent i = new Intent(this, ContextualTriggersService.class);
        startService(i);
    }
}
