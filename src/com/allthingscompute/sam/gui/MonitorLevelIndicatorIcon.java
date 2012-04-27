package com.allthingscompute.sam.gui;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.allthingscompute.sam.alert.AlertLevel;
import com.allthingscompute.sam.alert.AlertLevelChangeListener;

/**
 * This class is used to monitor the alert level, and update the icon to reflect
 * the alert level status.
 * 
 * @version 1.0
 * @author Documented by E. Internicola
 */
public class MonitorLevelIndicatorIcon extends JLabel implements
        AlertLevelChangeListener
{
    /** */
    private ImageIcon noStatusIcon = new ImageIcon("icons/stoplight_none.gif");

    /** */
    private ImageIcon lowStatusIcon = new ImageIcon("icons/stoplight_green.gif");

    /** */
    private ImageIcon mediumStatusIcon = new ImageIcon(
            "icons/stoplight_yellow.gif");

    /** */
    private ImageIcon highStatusIcon = new ImageIcon("icons/stoplight_red.gif");

    /**
     * Default Constructor - displays the icon corresponding to the provided
     * Alert Level.
     */
    public MonitorLevelIndicatorIcon(int alertLevel)
    {
        super();
        setIconType(alertLevel);
    }

    /**
     * This method will dispaly the icon corresponding to the provided Alert
     * Level.
     */
    public void setIconType(int alertLevel)
    {
        if (alertLevel == AlertLevel.NO_ALERT_LEVEL)
        {
            setIcon(noStatusIcon);
        } else if (alertLevel == AlertLevel.LOW_ALERT_LEVEL)
        {
            setIcon(lowStatusIcon);
        } else if (alertLevel == AlertLevel.MEDIUM_ALERT_LEVEL)
        {
            setIcon(mediumStatusIcon);
        } else if (alertLevel == AlertLevel.HIGH_ALERT_LEVEL)
        {
            setIcon(highStatusIcon);
        } else
        {
            setIcon(noStatusIcon);
        }
    }

    /**
     * Wrapper method for {@link #setIconType setIconType}.
     */
    public void alertLevelChanged(int alertLevel)
    {
        setIconType(alertLevel);
    }
}