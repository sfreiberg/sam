/*
 * Created on Feb 1, 2005
 */
package com.allthingscompute.sam.gui;

import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import com.allthingscompute.sam.alert.AlertTotalsPerProtocol;
import com.allthingscompute.sam.monitor.Monitor;
import com.allthingscompute.sam.monitor.MonitorChangeListener;
import com.allthingscompute.sam.monitor.TimedMonitor;

/**
 * @author sam
 */
public class ProtocolGraphPanel extends ChartPanel implements MonitorChangeListener
{
    JFreeChart chart = null;

    //DefaultPieDataset dataset = new DefaultPieDataset();
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    String[] seriesName = new String[] { "Alerts By Protocol" };

    //String[] protocols = new String[] { "TCP", "UDP", "ICMP", "UNKNOWN" };
    String[] protocols = new String[] { "TCP", "UDP", "ICMP" };

    Number[] data = new Integer[] { new Integer(0), new Integer(0),
            new Integer(0), new Integer(0) };

    public ProtocolGraphPanel()
    {
        //super(null, 200, 200, 100, 200, 1000, 1000, true, false,
        //        true, true, true, true);
        super(null);
        
        dataset.addValue(new Integer(0), "Alerts", "TCP");
        dataset.setValue(new Integer(0), "Alerts", "UDP");
        dataset.setValue(new Integer(0), "Alerts", "ICMP");

        chart = ChartFactory
                .createBarChart("Alerts By Protocol", null, null, dataset,
                        PlotOrientation.VERTICAL, false, true, false);
        CategoryPlot plot = (CategoryPlot)chart.getPlot();
        plot.getRenderer().setPaint(Color.BLUE);

        setChart(chart);
    }
    
    public void monitorChanged(Monitor monitor)
    {
        TimedMonitor mon = (TimedMonitor)monitor;
        alertByProtocolChanged(mon.getAlertTotalsPerProtocol());
    }

    public void alertByProtocolChanged(AlertTotalsPerProtocol totals)
    {
        dataset.addValue(new Integer(totals.getTcp()), "Alerts", "TCP");
        dataset.setValue(new Integer(totals.getUdp()), "Alerts", "UDP");
        dataset.setValue(new Integer(totals.getIcmp()), "Alerts", "ICMP");
    }
}
