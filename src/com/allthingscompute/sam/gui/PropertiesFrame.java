/*
 * PropertiesFrame.java
 *
 * Created on February 25, 2004, 12:26 PM
 */

package com.allthingscompute.sam.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.allthingscompute.sam.config.PropertiesManager;

/**
 * This class is responsible for the user interface to the properties.
 * 
 * @author E. Internicola
 */
public class PropertiesFrame extends JFrame implements ActionListener
{
    private PropertiesManager pm = null;

    private Properties p = null;

    private JLabel[] jlbLabels = null;

    private JTextField[] jtfTexts = null;

    private JScrollPane jspPane = null;

    private JPanel jpnlMain = null;

    private JButton jbtOK = null;

    private JButton jbtCancel = null;

    private Vector v = null;

    /** Creates a new instance of PropertiesFrame */
    public PropertiesFrame()
    {
        super("Properties");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        try
        {
            //first get the properties
            pm = PropertiesManager.getPropertiesManager();
            p = pm.getProperties();

            //now create the arrays of components
            jlbLabels = new JLabel[p.size()];
            jtfTexts = new JTextField[p.size()];

            //now create the single components
            jpnlMain = new JPanel(new GridLayout(p.size(), 2));
            jbtOK = new JButton("OK");
            jbtOK.addActionListener(this);
            jbtCancel = new JButton("Cancel");
            jbtCancel.addActionListener(this);

            //get the property keys
            v = new Vector();
            //Enumeration e = p.propertyNames();
            for (Enumeration e = p.propertyNames(); e.hasMoreElements();)
            {
                v.add(e.nextElement());
            }

            //now create the individual components and add them.
            for (int i = 0; i < p.size(); i++)
            {
                jlbLabels[i] = new JLabel((String) v.elementAt(i));
                jtfTexts[i] = new JTextField(p.getProperty((String) v
                        .elementAt(i)));
                jpnlMain.add(jlbLabels[i]);
                jpnlMain.add(jtfTexts[i]);
            }
            //configuring the JScrollPane
            jspPane = new JScrollPane(jpnlMain);
            jspPane
                    .setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            jspPane.setPreferredSize(new Dimension(300, 140));
            jspPane.validate();

            getContentPane().setLayout(new BorderLayout());
            getContentPane().add(jspPane, BorderLayout.NORTH);
            getContentPane().add(jbtOK, BorderLayout.WEST);
            getContentPane().add(jbtCancel, BorderLayout.EAST);
            setSize(400, 200);
            setResizable(false);
            setVisible(true);
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Exception loading data for Properties Frame:\t"
                    + e);
        }
    }

    /**
     * Update the properties, and write them to the file.
     * 
     * @see #com.allthingscompute.sam.conf.PropertiesManager.write
     */
    private void save()
    {
        for (int i = 0; i < v.size(); i++)
        {
            try
            {
                String key = v.elementAt(i).toString();
                pm.setValue(key, jtfTexts[i].getText());
            } catch (Exception e)
            {
                e.printStackTrace();
                System.out.println("Exception saving property:\t" + e);
            }
        }
        pm.write();
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == jbtOK)
        {
            save();
            dispose();
        } else if (e.getSource() == jbtCancel)
        {
            this.dispose();
        }
    }

}