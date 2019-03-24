package uk.ac.strath.contextualtriggers.exceptions;

public class LogSinkNotDefinedException extends Exception
{
    public LogSinkNotDefinedException()
    {
        super("Error: No Log Sink Defined");
    }


}
