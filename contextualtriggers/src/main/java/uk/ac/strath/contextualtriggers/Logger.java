package uk.ac.strath.contextualtriggers;

import android.widget.TextView;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import uk.ac.strath.contextualtriggers.exceptions.LogSinkNotDefinedException;

public class Logger
{
    private static Logger logger = null;
    TextView loggerText=null;
    private Logger()
    {
        logger=this;

    }
    public void setLogger(TextView loggerOut)
    {

        loggerText=loggerOut;

    }

    public synchronized void log(String message)
    {
        if(loggerText==null)
        {
            System.out.println("NOTE:: LOGGER SINK NOT DEFINED\n"+message);
        }
        loggerText.append(message);
    }

    public static Logger getInstance()
    {
        if(logger==null)
        {
            logger = new Logger();
        }
        return logger;
    }
}
