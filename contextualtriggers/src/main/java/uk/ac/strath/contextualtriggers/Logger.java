package uk.ac.strath.contextualtriggers;

import android.widget.TextView;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Logger
{
    public static Logger logger = null;
    TextView loggerText;
    public Logger(TextView logtext)
    {
        loggerText=logtext;
        logger=this;

    }
    public synchronized void log(String message)
    {
        loggerText.append(message);
    }

    public static Logger getInstance()
    {
        return logger;
    }
}
