package com.allthingscompute.sam.gui;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import com.allthingscompute.sam.log.Logger;
import com.allthingscompute.sam.monitor.Monitor;
import com.allthingscompute.sam.monitor.MonitorChangeListener;
import com.allthingscompute.sam.monitor.TimedMonitor;



/**
 * This class is used to display a graph for the user. It shows the most recent
 * attacks in terms of: the last 60 minutes, and the last 24 hours. <BR>
 * <BR>
 * Sample Depiction: <BR>
 * <IMG SRC="../GraphPanel.png" BORDER="0"> <BR>
 * The graph panel is the portion of the window that is boxed in red.
 * 
 * @version 1.0
 * @author Documented by E. Internicola
 */
public class GraphPanel extends JPanel implements MonitorChangeListener
{
    /** */
    private TimeSeries sixtyMinSeries;

    /** */
    private TimeSeries oneDaySeries;

    /** */
    private ChartPanel chartPanel;

    private ChartPanel oneDayChartPanel;

    private Box vertBox;

    /**
     * Default constructor - sets up and initializes all of the visual
     * components for the Graph.
     */
    public GraphPanel()
    {
        super();
        // Create last hour graph
        sixtyMinSeries = new TimeSeries("Number of Attacks",
                Millisecond.class);
        TimeSeriesCollection sixtyMinDataset = new TimeSeriesCollection(
                sixtyMinSeries);
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Attacks Last 60 Minutes", "Time", "Attacks", sixtyMinDataset,
                true, false, false);
        XYPlot plot = chart.getXYPlot();
        ValueAxis axis = plot.getDomainAxis();
        axis.setAutoRange(true);
        axis.setFixedAutoRange(3600000.0); // 60 minutes

        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(750, 150));

        // Create last 24 hours graph
        oneDaySeries = new TimeSeries("Number of Attacks",
                Millisecond.class);
        TimeSeriesCollection oneDayDataset = new TimeSeriesCollection(
                oneDaySeries);
        JFreeChart chart2 = ChartFactory
                .createTimeSeriesChart("Attacks Last 24 hours", "Time",
                        "Attacks", oneDayDataset, true, false, false);
        ValueAxis axis2 = chart2.getXYPlot().getDomainAxis();
        axis2.setAutoRange(true);
        axis2.setFixedAutoRange(86400000.0); // 24 hours
        //axis2.setCrosshairVisible(true);
        //axis2.setCrosshairLockedOnData(true);
        oneDayChartPanel = new ChartPanel(chart2);
        oneDayChartPanel.setPreferredSize(new Dimension(750, 150));

        vertBox = Box.createVerticalBox();
        vertBox.add(chartPanel);
        vertBox.add(oneDayChartPanel);
        add(vertBox);

    }

    /**
     * This method will update a Monitor object.
     * 
     * @see com.allthingscompute.sam.monitor.Monitor
     */
    public void monitorChanged(Monitor monitor)
    {
        try
        {
            TimedMonitor mon = (TimedMonitor) monitor;
            Integer i = new Integer(mon.getNumOfAttacksLast60Min());
            sixtyMinSeries.add(new Millisecond(), i);
            oneDaySeries.add(new Millisecond(), i);
        } catch (Exception e)
        {
            Logger.writeToLog(e.getMessage());
        }
    }

    /**
     * Retreive the ChartPanel object.
     */
    public void setSizes(int w, int h)
    {
        vertBox.setSize(w, h);
        chartPanel.setPreferredSize(new Dimension(w, h / 2));
        oneDayChartPanel.setPreferredSize(new Dimension(w, h / 2));
    }
}