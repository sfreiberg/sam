package com.allthingscompute.sam.alert;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.allthingscompute.sam.log.Logger;

/**
 * This class is to be used similarly to a JLabel, although it is actually used
 * as a container for an "Alert Icon", with which it displays images. The image
 * that is shown is determined by the Alert Level.
 * 
 * @author Sam, Documented by E. Internicola
 * @see AlertLevelChangeListener
 * @see AlertLevel
 */
public class VisualAlert extends JLabel implements AlertLevelChangeListener
{
    String noWarningFile, lowWarningFile, medWarningFile, highWarningFile;

    private int alertLevel;

    /**
     * This constructor does the same thing as the other constructor(
     * {@link #VisualAlert(int,String,String,String,String) VisualAlert}) but
     * it sets the first parameter (alertLevel) to
     * {@link AlertLevel#NO_ALERT_LEVEL NO_ALERT_LEVEL}.
     * 
     * @see #VisualAlert(int,String,String,String,String)
     */
    public VisualAlert(String noWarning, String lowWarning, String medWarning,
            String highWarning)
    {
        this(AlertLevel.NO_ALERT_LEVEL, noWarning, lowWarning, medWarning,
                highWarning);
    }

    /**
     * VisualAlert constructor that takes 4 String objects, and sets the warning
     * level to the provided alert level. <BR>
     * This constructor takes 5 parameters:
     * <OL>
     * <LI>int alertLevel - the alert level to start the VisualAlert off with
     * </LI>
     * <LI>String noWarning - The text used to denote that there are no
     * warnings</LI>
     * <LI>String lowWarning - the text used to denote that there is a low
     * warning</LI>
     * <LI>String medWarning - the text used to denote that there is a medium
     * warning level</LI>
     * <LI>String highWarning - the text used to denote that there is a high
     * warning level</LI>
     * </OL>
     * If you don't care what the alert level is set to then you should use the
     * former constructor:
     * {@link #VisualAlert(String,String,String,String) VisualAlert}which will
     * default the alertLevel to
     * {@link AlertLevel#NO_ALERT_LEVEL NO_ALERT_LEVEL}.
     * 
     * @see AlertLevel
     * @see #VisualAlert(String,String,String,String)
     */
    public VisualAlert(int alertLevel, String noWarning, String lowWarning,
            String medWarning, String highWarning)
    {
        super();
        noWarningFile = noWarning;
        lowWarningFile = lowWarning;
        medWarningFile = medWarning;
        highWarningFile = highWarning;
        setAlertIcon(alertLevel);
    }

    /**
     * This method will set the Alert Icon to the specified Alert Level when
     * this event is fired. This event is inherited from
     * {@link AlertLevelChangeListener AlertLevelChangeListener}.
     * 
     * @see AlertLevelChangeListener#alertLevelChanged
     */
    public void alertLevelChanged(int alertLevel)
    {
        setAlertIcon(alertLevel);
    }

    /**
     * This method will modify the Alert Icon based on the alertLevel passed as
     * a parameter.
     */
    private void setAlertIcon(int alertLevel)
    {
        ImageIcon icon = null;
        if (alertLevel == AlertLevel.LOW_ALERT_LEVEL)
            icon = new ImageIcon(lowWarningFile);
        else if (alertLevel == AlertLevel.MEDIUM_ALERT_LEVEL)
            icon = new ImageIcon(medWarningFile);
        else if (alertLevel == AlertLevel.HIGH_ALERT_LEVEL)
            icon = new ImageIcon(highWarningFile);
        else
            icon = new ImageIcon(noWarningFile);
        if (icon == null)
        {
            Logger.writeToLog("I couldn't find that icon");
        } else
        {
            setIcon(icon);
        }
        repaint();
    }
}