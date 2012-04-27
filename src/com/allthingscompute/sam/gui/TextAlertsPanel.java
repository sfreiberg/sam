package com.allthingscompute.sam.gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import com.allthingscompute.sam.monitor.Monitor;
import com.allthingscompute.sam.monitor.MonitorChangeListener;
import com.allthingscompute.sam.monitor.TimedMonitor;

/**
 * @author sfreiberg, Documented by E. Internicola <BR>
 *         Date Jun 26, 2002
 */
public class TextAlertsPanel extends JPanel implements MonitorChangeListener
{
    JLabel last5Min = new JLabel("No Data", SwingConstants.LEFT);

    JLabel last15Min = new JLabel("No Data", SwingConstants.LEFT);

    JLabel last30Min = new JLabel("No Data", SwingConstants.LEFT);

    JLabel last60Min = new JLabel("No Data", SwingConstants.LEFT);

    JLabel total = new JLabel("No Data", SwingConstants.LEFT);

    JLabel lastUpdate = new JLabel("Last Update: No Data");

    Font font = new Font("serif", Font.BOLD, 24);

    public TextAlertsPanel()
    {
        super();
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        // set font
        last5Min.setFont(font);
        last15Min.setFont(font);
        last30Min.setFont(font);
        last60Min.setFont(font);
        total.setFont(font);

        // Create left side
        JLabel last5Label = new JLabel("Last 5 min:   ", SwingConstants.RIGHT);
        JLabel last15Label = new JLabel("Last 15 min:   ", SwingConstants.RIGHT);
        JLabel last30Label = new JLabel("Last 30 min:   ", SwingConstants.RIGHT);
        JLabel last60Label = new JLabel("Last 60 min:   ", SwingConstants.RIGHT);
        JLabel totalLabel = new JLabel("Total:   ", SwingConstants.RIGHT);
        last5Label.setFont(font);
        last15Label.setFont(font);
        last30Label.setFont(font);
        last60Label.setFont(font);
        totalLabel.setFont(font);

        // Put it all together
        setBorder(new TitledBorder("Recent Alerts"));

        // Build the left side
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 1;
        constraints.gridwidth = 2;
        constraints.gridy = 0;
        add(last5Label, constraints);
        constraints.gridy = 1;
        add(last15Label, constraints);
        constraints.gridy = 2;
        add(last30Label, constraints);
        constraints.gridy = 3;
        add(last60Label, constraints);
        constraints.gridy = 4;
        add(totalLabel, constraints);

        // Build the right side
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = 1;
        constraints.gridx = 2;
        constraints.gridy = 0;
        add(last5Min, constraints);
        constraints.gridy = 1;
        add(last15Min, constraints);
        constraints.gridy = 2;
        add(last30Min, constraints);
        constraints.gridy = 3;
        add(last60Min, constraints);
        constraints.gridy = 4;
        add(total, constraints);

        // Build the lower half
        constraints.gridwidth = 3;
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.anchor = GridBagConstraints.CENTER;
        add(lastUpdate, constraints);
    }

    public void monitorChanged(Monitor monitor)
    {
        TimedMonitor timedMonitor = (TimedMonitor) monitor;
        last5Min.setText(timedMonitor.getNumOfAttacksLast5Min());
        last15Min.setText(timedMonitor.getNumOfAttacksLast15Min());
        last30Min.setText(timedMonitor.getNumOfAttacksLast30Min());
        last60Min.setText(timedMonitor.getNumOfAttacksLast60Min());
        total.setText(timedMonitor.getTotalNumOfAttacks());
        lastUpdate.setText("Last Update: " + timedMonitor.getLastUpdatedTime());
    }
}