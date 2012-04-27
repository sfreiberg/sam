/*
 * Created on Jan 25, 2005
 */
package com.allthingscompute.sam.gui;

import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import com.allthingscompute.sam.alert.AlertPriorityTotals;
import com.allthingscompute.sam.monitor.Monitor;
import com.allthingscompute.sam.monitor.MonitorChangeListener;
import com.allthingscompute.sam.monitor.TimedMonitor;


/**
 * @author sam
 */
public class PriorityGraphPanel extends ChartPanel implements MonitorChangeListener
{
    JFreeChart chart = null;
    
    public PriorityGraphPanel()
    {
        super(null);
        
        DefaultPieDataset dataset = new DefaultPieDataset();
        chart = ChartFactory.createPieChart("Alert Severity", dataset, false,
                true, false);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionPaint(0, Color.RED);
        plot.setSectionPaint(1, Color.YELLOW);
        plot.setSectionPaint(2, Color.GREEN);
        plot.setSectionPaint(3, Color.BLUE);
        
        setChart(chart);
    }
    
    public void monitorChanged(Monitor monitor)
    {
        TimedMonitor m = (TimedMonitor)monitor;
        AlertPriorityTotals totals = m.getAlertPriorityTotals();
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("High", totals.getHigh());
        dataset.setValue("Medium", totals.getMedium());
        dataset.setValue("Low", totals.getLow());
        dataset.setValue("Unknown", totals.getUnknown());
        PiePlot plot = (PiePlot) chart.getPlot();

        plot.setDataset(dataset);
    }
}
