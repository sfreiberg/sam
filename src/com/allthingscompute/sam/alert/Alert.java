package com.allthingscompute.sam.alert;

/**
 * This class keeps track of an Alert Number.
 * 
 * @author Sam
 */
public abstract class Alert
{
    /** current alert number */
    int currentAlert;

    /**
     * pulbic method to retreive the current alert value.
     */
    public int getCurrentAlert()
    {
        return currentAlert;
    }

    /**
     * public method to set the current alert value.
     */
    public void setCurrentAlert(int alert)
    {
        currentAlert = alert;
    }
}