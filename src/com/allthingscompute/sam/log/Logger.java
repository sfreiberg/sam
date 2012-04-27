package com.allthingscompute.sam.log;

import java.util.ArrayList;

/**
 * This class is used to keep track of, write to, and clear out lists of
 * loggable objects. You first must add one or more loggers to the list, then
 * whenever you call {@link #writeToLog writeToLog}or
 * {@link #clearLog clearLog}, then the call will be made to each loggable
 * object in the collection .
 * 
 * @author Sam, Documented by E. Internicola
 */
public class Logger
{
    /** The list of loggable objects. */
    private static ArrayList loggers = new ArrayList();

    /**
     * This method writes to each of the Loggable objects in the array list of
     * loggers.
     */
    public static void writeToLog(String s)
    {
        for (int i = 0; i < loggers.size(); i++)
        {
            Loggable l = (Loggable) loggers.get(i);
            l.writeToLog(s);
        }
    }

    /**
     * This method clears out each of the loggable objects in the array list of
     * loggers.
     */
    public static void clearLog()
    {
        for (int i = 0; i < loggers.size(); i++)
        {
            Loggable l = (Loggable) loggers.get(i);
            l.clearLog();
        }
    }

    /**
     * This method allows a Loggable object to be added to the list of loggable
     * objects (meaning that a call to "write to log" will be passed to the
     * loggable object
     */
    public static void addLoggable(Loggable l)
    {
        loggers.add(l);
    }

    /**
     * This method will remove a Loggable object from the list of loggable
     * objects, meaning that it will no longer receive log events (add, clear).
     */
    public static void removeLoggable(Loggable l)
    {
        loggers.remove(l);
    }
}