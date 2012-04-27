/*
 * Created on Jan 24, 2005
 */
package com.allthingscompute.sam.alert;


/**
 * @author sam
 */
public class AlertPriorityTotals
{
    int high = 0;

    int medium = 0;

    int low = 0;

    int unknown = 0;

    /**
     * @return Returns the high.
     */
    public int getHigh()
    {
        return high;
    }

    /**
     * @return Returns the low.
     */
    public int getLow()
    {
        return low;
    }

    /**
     * @return Returns the medium.
     */
    public int getMedium()
    {
        return medium;
    }

    /**
     * @return Returns the unknown.
     */
    public int getUnknown()
    {
        return unknown;
    }

    public void setTotal(int alertLevel, int alertTotal)
    {
        if (alertLevel == AlertPriority.HIGH)
            high = alertTotal;
        else if (alertLevel == AlertPriority.MEDIUM)
            medium = alertTotal;
        else if (alertLevel == AlertPriority.LOW)
            low = alertTotal;
        else
            unknown = alertTotal;
    }
}
