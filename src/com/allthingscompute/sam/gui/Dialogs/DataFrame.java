/*
 * DataFrame.java
 *
 * Created on February 21, 2004, 6:13 PM
 */

package com.allthingscompute.sam.gui.Dialogs;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowStateListener;
import java.sql.ResultSet;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

import com.allthingscompute.sam.database.Report.ReportEngine;

/**
 * This class is used to display the data portion of an attack.
 * 
 * @author E. Internicola
 */
public class DataFrame extends JFrame implements Runnable, WindowStateListener,
        ComponentListener
{
    private Font normalFont = new Font("Dialog", 0, 12);

    private JPanel panel = null;

    private JLabel[] jlbLabels = null;

    private JTextArea[] jtaData = null;

    private JScrollPane[] areaScrollPane = null;

    private ResultSet objRst = null;

    private long sid;

    private long cid;

    /** Creates a new instance of DataFrame */
    public DataFrame(long sid, long cid)
    {
        this.sid = sid;
        this.cid = cid;
        Thread t = new Thread(this);
        t.start();
    }

    /**
     * This method will return a character for a given hex value. If the
     * character is beyond the "non-garbage" character range, then a period is
     * returned.
     */
    private char getCharFromHex(String hexPair)
    {
        try
        {
            int value = Integer.parseInt(hexPair, 16);
            if (value >= 33 && value <= 126)
            {
                return (char) value;
            }
            return '.';
        } catch (Exception e)
        {
            return '.';
        }
    }

    public void run()
    {
        panel = new JPanel(new GridBagLayout());
        //Titles
        jlbLabels = new JLabel[3];
        jlbLabels[0] = new JLabel("Signature:\t");
        jlbLabels[1] = new JLabel("Data (Hex):\t");
        jlbLabels[2] = new JLabel("Data (Str):\t");

        jtaData = new JTextArea[3];
        areaScrollPane = new JScrollPane[3];

        for (int i = 0; i < 3; i++)
        {
            jtaData[i] = new JTextArea();
            jtaData[i].setFont(normalFont);
            jtaData[i].setLineWrap(true);
            areaScrollPane[i] = new JScrollPane(jtaData[i]);
            areaScrollPane[i]
                    .setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        }
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        panel.setBorder(new TitledBorder("Data for " + cid));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(2, 5, 5, 5);
        //c.anchor = c.WEST;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx = 0;
        c.gridy = 0;
        panel.add(jlbLabels[0], c);
        c.gridy = GridBagConstraints.RELATIVE;
        panel.add(jlbLabels[1], c);
        panel.add(jlbLabels[2], c);
        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        for (int i = 0; i < 3; i++)
        {
            panel.add(areaScrollPane[i], c);
        }

        try
        {
            objRst = ReportEngine.getSignature(sid, cid);
            if (objRst.next())
            {
                jtaData[0].setText(objRst.getString("sig_name"));
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Exception retreiving Signature for cid:\t"
                    + cid);
        }
        try
        {
            objRst = ReportEngine.getData(sid, cid);
            if (objRst.next())
            {
                jtaData[1].setText(objRst.getString("data_payload"));

                String data = objRst.getString("data_payload");
                String chars = "";
                for (int i = 0; i < data.length(); i += 2)
                {
                    chars += getCharFromHex(data.substring(i, i + 2));
                }
                jtaData[2].setText(chars);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Exception retreiving Data for cid:\t" + cid);
        }
        getContentPane().add(panel);
        setSize(500, 350);
        resize();
        addComponentListener(this);
        addWindowStateListener(this);
        setVisible(true);
    }

    /**
     * This method handles resizing the components to make them fit inside the
     * window nicely.
     */
    private void resize()
    {
        panel
                .setPreferredSize(new Dimension(getWidth() - 10,
                        getHeight() - 10));
        int w = getWidth() - 60;
        int h = (getHeight() / 3) - 10;
        for (int i = 0; i < 3; i++)
        {
            areaScrollPane[i].setMinimumSize(new Dimension(w - 50, h - 20));
            areaScrollPane[i].setPreferredSize(new Dimension(w, h));
        }
    }

    public void windowStateChanged(java.awt.event.WindowEvent e)
    {
        componentResized(new ComponentEvent(this,
                ComponentEvent.COMPONENT_RESIZED));
    }

    public void componentHidden(java.awt.event.ComponentEvent e)
    {
    }

    public void componentMoved(java.awt.event.ComponentEvent e)
    {
    }

    public void componentResized(java.awt.event.ComponentEvent e)
    {
        resize();
    }

    public void componentShown(java.awt.event.ComponentEvent e)
    {
    }
}