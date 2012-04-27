package com.allthingscompute.sam.monitor;

import java.util.ArrayList;

import com.allthingscompute.sam.alert.AlertLevel;
import com.allthingscompute.sam.alert.AlertLevelChangeListener;

/**
 * 
 * @author Sam, Documented by E. Internicola
 */
public abstract class Monitor extends Thread
{
    /** A list of MonitorChangeListener objects. */
    private ArrayList monitorChangeListeners;

    /** A list of AlertLevelChangeListener objects. */
    private ArrayList alertLevelChangeListeners;

    /** The Current Alert Level. */
    private int currentAlertLevel;

    /*
     * Default constructor; sets the current alert level to
     * {@link com.allthingscompute.sam.alert.AlertLevel.NO_ALERT_LEVEL NO_ALERT_LEVEL}.
     * 
     * @see #Monitor(int)
     */
    public Monitor()
    {
        this(AlertLevel.NO_ALERT_LEVEL);
    }

    /**
     * Constructor that takes an initial alert level. This constructor
     * initializes the list of monitor change listeners, and the list of alert
     * level change listeners.
     */
    public Monitor(int alertLevel)
    {
        super();
        monitorChangeListeners = new ArrayList();
        alertLevelChangeListeners = new ArrayList();
        currentAlertLevel = alertLevel;
    }

    /**
     * Public method to add a MonitorChangeListener object to the list of
     * monitor change listeners.
     * 
     * @see com.allthingscompute.sam.monitor.MonitorChangeListener
     */
    public void addMonitorChangeListener(MonitorChangeListener listener)
    {
        monitorChangeListeners.add(listener);
    }

    /**
     * Public method to remove a MonitorChangeListener object from the list of
     * monitor change listeners.
     * 
     * @see com.allthingscompute.sam.monitor.MonitorChangeListener
     */
    public void removeMonitorChangeListener(MonitorChangeListener listener)
    {
        monitorChangeListeners.remove(listener);
    }

    /**
     * Public method to retreive the entire list of MonitorChangeListener
     * objects.
     * 
     * @see com.allthingscompute.sam.monitor.MonitorChangeListener
     */
    public ArrayList getMonitorChangeListeners()
    {
        return monitorChangeListeners;
    }

    /**
     * Call this method when the monitor gets updated to let all the registered
     * listeners know.
     */
    public void fireMonitorChanged(Monitor monitor)
    {
        for (int i = 0; i < monitorChangeListeners.size(); i++)
        {
            MonitorChangeListener listener = (MonitorChangeListener) monitorChangeListeners
                    .get(i);
            listener.monitorChanged(monitor);
        }
    }

    /**
     * Public method to add an AlertLevelChangeListener object to the list of
     * alert level change listeners.
     * 
     * @see com.allthingscompute.sam.alert.AlertLevelChangeListener
     */
    public void addAlertLevelChangeListener(AlertLevelChangeListener listener)
    {
        alertLevelChangeListeners.add(listener);
    }

    /**
     * Public method to remove an AlertLevelChangeListener object from the list
     * of alert level change listeners.
     * 
     * @see com.allthingscompute.sam.alert.AlertLevelChangeListener
     */
    public void removeAlertLevelChangeListener(AlertLevelChangeListener listener)
    {
        alertLevelChangeListeners.remove(listener);
    }

    /**
     * Public method to retreive the entire list of AlertLevelChangeListener
     * objects.
     * 
     * @see com.allthingscompute.sam.alert.AlertLevelChangeListener
     */
    public ArrayList getAlertLevelChangeListeners()
    {
        return alertLevelChangeListeners;
    }

    public void fireAlertChangeListeners(int alertLevel)
    {
        for (int i = 0; i < alertLevelChangeListeners.size(); i++)
        {
            AlertLevelChangeListener listener = (AlertLevelChangeListener) alertLevelChangeListeners
                    .get(i);
            listener.alertLevelChanged(alertLevel);
        }
    }

    public int getCurrentAlertLevel()
    {
        return currentAlertLevel;
    }

    public void setCurrentAlertLevel(int alertLevel)
    {
        currentAlertLevel = alertLevel;
    }

    public abstract void update();

    public abstract void run();
}