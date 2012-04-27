/*
 * Created on Feb 1, 2005
 */
package com.allthingscompute.sam.gui;

import java.awt.BasicStroke;
import java.awt.Color;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.ThermometerPlot;
import org.jfree.data.general.DefaultValueDataset;

import com.allthingscompute.sam.config.PropertiesManager;
import com.allthingscompute.sam.monitor.Monitor;
import com.allthingscompute.sam.monitor.MonitorChangeListener;
import com.allthingscompute.sam.monitor.TimedMonitor;

/**
 * @author sam
 */
public class ThermometerPanel extends ChartPanel implements MonitorChangeListener
{
    JFreeChart chart = null;

    ThermometerPlot plot = null;

    DefaultValueDataset data = null;

    double warningRange = 15;

    double criticalRange = 35;

    double maxRange = 100;

    public ThermometerPanel()
    {
        super(null, 150, 300, 100, 200, 1000, 1000,
                true, false, true, true, true, true);
        
        
        try
        {
            warningRange = Integer.parseInt(PropertiesManager.getPropertiesManager().getValue("alertlevel.medium"));
            criticalRange = Integer.parseInt(PropertiesManager.getPropertiesManager().getValue("alertlevel.high"));
            maxRange = criticalRange + 5;
        } catch (NumberFormatException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        // Build up graph
        data = new DefaultValueDataset(0);
        plot = new ThermometerPlot(data);
        plot.setMercuryPaint(Color.RED);
        plot.setSubrangePaint(ThermometerPlot.WARNING, Color.YELLOW);
        plot.setFollowDataInSubranges(false);
        plot.setThermometerStroke(new BasicStroke(2));
        plot.setThermometerPaint(Color.LIGHT_GRAY);
        plot.setUnits(ThermometerPlot.NONE);
        plot.setRange(0.0d, maxRange);
        plot.setSubrange(ThermometerPlot.NORMAL, 0, warningRange);
        plot.setSubrange(ThermometerPlot.WARNING, warningRange, criticalRange);
        plot.setSubrange(ThermometerPlot.CRITICAL, criticalRange, maxRange);
        plot.setFollowDataInSubranges(false);

        chart = new JFreeChart("Alerts", plot);

        setChart(chart);
    }
    
    public void monitorChanged(Monitor monitor)
    {
        TimedMonitor mon = (TimedMonitor)monitor;
        data.setValue(new Integer(mon.getNumOfAttacksLast5Min()));
    }
}
