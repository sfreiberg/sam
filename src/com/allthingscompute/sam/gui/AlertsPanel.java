package com.allthingscompute.sam.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.io.File;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.allthingscompute.sam.alert.AlertLevel;
import com.allthingscompute.sam.alert.AlertLevelChangeListener;
import com.allthingscompute.sam.alert.AudioAlert;
import com.allthingscompute.sam.alert.EmailAlert;
import com.allthingscompute.sam.log.Logger;
import com.allthingscompute.sam.monitor.Monitor;
import com.allthingscompute.sam.monitor.MonitorChangeListener;
import com.allthingscompute.sam.monitor.TimedMonitor;

/**
 * This object displays recent attack information (last 5-60 minutes). It is not
 * used directly; it is embedded in the
 * {@link com.allthingscompute.sam.gui.MainFrame MainFrame}object.
 * 
 * @version 1.0
 * @author Documented by E. Internicola <BR>
 *         Date 3/25/2003
 */
public class AlertsPanel extends JPanel implements MonitorChangeListener,
        AlertLevelChangeListener
{
    /** The Indicator Icon. */
    private MonitorLevelIndicatorIcon indicatorIcon;

    /** Label - last 5 minutes. */
    private JLabel last5 = new JLabel("Attacks last 5  minutes:  ");

    /** Label - last 15 minutes. */
    private JLabel last15 = new JLabel("Attacks last 15 minutes: ");

    /** Label - last 30 minutes. */
    private JLabel last30 = new JLabel("Attacks last 30 minutes: ");

    /** Label - last 60 minutes. */
    private JLabel last60 = new JLabel("Attacks last 60 minutes: ");

    /** Label - Total # of attacks. */
    private JLabel totalAttacks = new JLabel("Total number of attacks: ");

    /** Label - last updated. */
    private JLabel lastUpdated = new JLabel("Last updated: ");

    /** Font to be used (serif BOLD 24). */
    private Font font = new Font("serif", Font.BOLD, 24);

    private Box alertStatsPanel;

    private Box alertIconPanel;

    private AudioAlert audioAlert;

    private EmailAlert emailAlert;

    /**
     * Constructor that takes a monitor object.
     * 
     * @see com.allthingscompute.sam.monitor.Monitor
     */
    public AlertsPanel(Monitor monitor)
    {
        super(new BorderLayout(20, 20));

        last5.setFont(font);
        last15.setFont(font);
        last30.setFont(font);
        last60.setFont(font);
        totalAttacks.setFont(font);
        lastUpdated.setFont(new Font("serif", Font.PLAIN, 12));

        indicatorIcon = new MonitorLevelIndicatorIcon(AlertLevel.NO_ALERT_LEVEL);
        monitor.addAlertLevelChangeListener(indicatorIcon);

        audioAlert = new AudioAlert(new File("wav/green.wav"), new File(
                "wav/yellow.wav"), new File("wav/red.wav"));
        try
        {
            emailAlert = new EmailAlert();
        } catch (Exception exception)
        {
            Logger.writeToLog(exception.getMessage());
        }
        monitor.addAlertLevelChangeListener(audioAlert);
        monitor.addAlertLevelChangeListener(emailAlert);

        alertIconPanel = Box.createVerticalBox();
        alertIconPanel.add(indicatorIcon);
        Box.createVerticalGlue();

        alertStatsPanel = Box.createVerticalBox();
        alertStatsPanel.add(last5);
        alertStatsPanel.add(last15);
        alertStatsPanel.add(last30);
        alertStatsPanel.add(last60);
        alertStatsPanel.add(totalAttacks);
        alertStatsPanel.add(lastUpdated);
        Box.createVerticalGlue();

        add(alertIconPanel, BorderLayout.WEST);
        add(alertStatsPanel, BorderLayout.CENTER);
        setBorder(new TitledBorder("Threat status"));

    }

    /**
     * Public method to set the icon type when the alert level changes.
     */
    public void alertLevelChanged(int alertLevel)
    {
        indicatorIcon.setIconType(alertLevel);
    }

    /**
     * Public method to update the display when a monitor changes. Each of the
     * "Attacks" labels is updated to reflect the state of the monitor change.
     */
    public void monitorChanged(Monitor monitor)
    {
        TimedMonitor timedMonitor = (TimedMonitor) monitor;
        last5.setText("Attacks last 5  minutes:  "
                + timedMonitor.getNumOfAttacksLast5Min());
        last15.setText("Attacks last 15  minutes:  "
                + timedMonitor.getNumOfAttacksLast15Min());
        last30.setText("Attacks last 30  minutes:  "
                + timedMonitor.getNumOfAttacksLast30Min());
        last60.setText("Attacks last 60  minutes:  "
                + timedMonitor.getNumOfAttacksLast60Min());
        totalAttacks.setText("Total number of attacks: "
                + timedMonitor.getTotalNumOfAttacks());
        lastUpdated.setText("Last updated: "
                + timedMonitor.getLastUpdatedTime());
    }
}