package com.allthingscompute.sam.log;

/**
 * This interface defines a common interface for subclasses to write to and
 * clear logs.
 * 
 * @author Sam, Documented by E. Internicola
 */
public interface Loggable
{
    /**
     * This method must be implemented by a subclass, and will be used to append
     * a line to a log.
     */
    public void writeToLog(String s);

    /**
     * This method must be implemented by a subclass, and will be used to clear
     * out a log.
     */
    public void clearLog();
}