package com.allthingscompute.sam.log;

import java.io.File;
import java.io.FileOutputStream;

import com.allthingscompute.sam.config.PropertiesManager;

/**
 * This class is used to write events to a log file.
 * 
 * @author E. Internicola <BR>
 *         5/6/2003
 */
public class LogFileLogger implements Loggable
{

    private static LogFileLogger lfl = null;

    private File LogFile;

    private FileOutputStream fos = null;

    /**
     * Default Constructor - sets the log file and static reference to this
     * method.
     */
    private LogFileLogger()
    {
        try
        {
            lfl = this;
            LogFile = new File(PropertiesManager.getPropertiesManager()
                    .getValue("LogFile"));
            fos = new FileOutputStream(LogFile, true);
        } catch (Exception e)
        {
            Logger.writeToLog(e.getMessage());
        }
    }

    public static LogFileLogger getLogFileLogger()
    {
        if (lfl == null)
        {
            lfl = new LogFileLogger();
        }
        return lfl;
    }

    /**
     *  
     */
    public synchronized void writeToLog(String s)
    {
        try
        {
            fos.write(s.getBytes());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     *  
     */
    public synchronized void clearLog()
    {
        try
        {
            fos.close();
            LogFile.delete();
            lfl = null;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}