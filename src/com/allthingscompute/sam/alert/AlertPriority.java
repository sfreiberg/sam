/*
 * Created on Jan 24, 2005
 */
package com.allthingscompute.sam.alert;

/**
 * @author sam
 */
public class AlertPriority
{
    public static final int HIGH = 1;

    public static final int MEDIUM = 2;

    public static final int LOW = 3;

    public static final int UNKNOWN = 5;

    int alertLevel = UNKNOWN;

    public AlertPriority()
    {
        // do nothing. alertLevel will default to UNKNOWN
    }

    public AlertPriority(int alertLevel)
    {
        setAlertLevel(alertLevel);
    }

    public void setAlertLevel(int alertLevel)
    {
        if (alertLevel == HIGH || alertLevel == MEDIUM || alertLevel == LOW)
            this.alertLevel = alertLevel;
        else
            this.alertLevel = UNKNOWN;
    }

    public int getAlertLevel()
    {
        return alertLevel;
    }

    public String getShortDescription()
    {
        return getShortDescription(alertLevel);
    }

    public static final String getShortDescription(int alertLevel)
    {
        if (alertLevel == HIGH)
            return "High";
        else if (alertLevel == MEDIUM)
            return "Medium";
        else if (alertLevel == LOW)
            return "Low";
        else
            return "Unknown";
    }
}
