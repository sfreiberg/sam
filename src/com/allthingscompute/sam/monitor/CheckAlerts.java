package com.allthingscompute.sam.monitor;

import java.sql.Connection;

import com.allthingscompute.sam.gui.AlertsPanel;

/**
 * This class doesn't appear to do much of anything at the moment; I'm not sure
 * why this is.
 * 
 * @author sfreiberg, Documented by E. Internicola Date Jun 19, 2002
 */
public class CheckAlerts extends Thread
{
    /** Connection to a snort database. */
    private Connection con = null;

    /** AlertPanel object (apparently to signal when the alert level changes). */
    private AlertsPanel alertPanel;

    /**
     * public method to set the Alert Panel.
     */
    public void setAlertsPanel(AlertsPanel alertPanel)
    {
        this.alertPanel = alertPanel;
    }

    /**
     * Since this program is a thread, you make it run by calling the start
     * method.
     * 
     * @see java.lang.Runnable#run()
     */
    public void run()
    {

    }

}