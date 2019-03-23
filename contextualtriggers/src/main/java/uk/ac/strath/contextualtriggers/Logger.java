package uk.ac.strath.contextualtriggers;

import android.widget.TextView;

public class Logger
{
    public static Logger logger = null;
    TextView loggerText;
    public Logger(TextView logtext)
    {
        loggerText=logtext;
        logger=this;

    }
    public void log(String message)
    {
        loggerText.append(message);
    }

    public static Logger getInstance()
    {
        return logger;
    }
}
