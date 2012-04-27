package com.allthingscompute.sam.monitor;

/**
 * @author sfreiberg Date Jun 18, 2002
 */

import java.util.Vector;

public class MonitorLevel
{
    // Constant warning levels
    public static final int NO_WARNING = 0;

    public static final int LOW_WARNING = 1;

    public static final int MEDIUM_WARNING = 2;

    public static final int HIGH_WARNING = 3;

    // current warning level
    private int warningLevel;

    // change listeners that have registered
    private Vector monitorLevelChangeListeners;

    public MonitorLevel()
    {
        this(MonitorLevel.NO_WARNING);
    }

    public MonitorLevel(int warningLevel)
    {
        setWarningLevel(warningLevel);
        monitorLevelChangeListeners = new Vector();
    }

    public int getWarningLevel()
    {
        return warningLevel;
    }

    public void setWarningLevel(int warningLevel)
    {
        this.warningLevel = warningLevel;
    }

    public void addMonitorLevelChangeListener(
            MonitorLevelChangeListener listener)
    {
        monitorLevelChangeListeners.add(listener);
    }

    public void removeMonitorLevelChangeListener(
            MonitorLevelChangeListener listener)
    {
        monitorLevelChangeListeners.remove(listener);
    }

    public void fireMonitorStatusLevelChanged()
    {
        for (int i = 0; i < monitorLevelChangeListeners.size(); i++)
        {
            MonitorLevelChangeListener listener = (MonitorLevelChangeListener) monitorLevelChangeListeners
                    .get(i);
            listener.monitorStatusChanged(this);
        }
    }
}