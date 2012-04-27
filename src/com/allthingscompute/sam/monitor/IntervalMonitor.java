package com.allthingscompute.sam.monitor;

/**
 * Yet another class that doesn't seem to do anything at the moment.
 * 
 * @author sfreiberg, Documented by E. Internicola <BR>
 *         Date Jul 5, 2002
 */
public class IntervalMonitor extends Monitor
{
    /** interval at which to update. */
    private int updateInterval;

    /** the time that the last update took place? */
    private int lastUpdate;

    /**
     * Does nothing yet...
     * 
     * @see com.allthingscompute.sam.monitor.Monitor#update()
     */
    public void update()
    {

    }

    /**
     * This class is a subclass of java.lang.Thread, so to get it going you must
     * call the start method which will start the thread, and invoke this run
     * method. At the moment though; this class does absolutely nothing.
     */
    public void run()
    {

    }
}