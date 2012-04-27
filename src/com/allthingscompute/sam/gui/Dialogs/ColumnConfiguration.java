/*
 *  ColumnConfiguration.java
 *
 *  Created on March 16, 2004, 10:24 AM
 */
package com.allthingscompute.sam.gui.Dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Date;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.allthingscompute.sam.config.PropertiesManager;
import com.allthingscompute.sam.gui.Tools.AttackColumnBuilder;

/**
 * This class is used to configure the columns that are visible on the Attack
 * Tables.
 * 
 * @author E. Internicola
 */
public class ColumnConfiguration extends JDialog implements MouseListener,
        ActionListener
{
    private PropertiesManager pm = null;

    private Vector hidden = null;

    private Vector shown = null;

    private JList jlHidden = null;

    private JScrollPane jspHidden = null;

    private JList jlShown = null;

    private JScrollPane jspShown = null;

    private JPanel[] jpParts = null;

    private JButton[] jbButtons = null;

    private JLabel[] jlLabels = null;

    private Date lastClick = null;

    private Frame owner = null;

    /**
     * Constructor - this does everything.
     */
    public ColumnConfiguration(Frame owner, boolean modal)
    {
        super(owner, modal);
        try
        {
            this.owner = owner;
            //Get rid of this next line.
            setDefaultCloseOperation(JDialog.EXIT_ON_CLOSE);

            //Read the Properties.
            pm = PropertiesManager.getPropertiesManager();
            String cols = pm.getValue("AttackColumns");
            if (cols == null)
            {
                cols = AttackColumnBuilder
                        .intVectorToString(AttackColumnBuilder
                                .getIntVectorFromInts(AttackColumnBuilder
                                        .getAllColumnsAsInts()));
            }
            // create lists
            hidden = AttackColumnBuilder
                    .getIntVectorFromInts(AttackColumnBuilder
                            .getAllColumnsAsInts());
            shown = AttackColumnBuilder.stringToIntVector(cols);

            jpParts = new JPanel[3];
            hidden = AttackColumnBuilder.intersectWithUniversalSet(shown);
            jlHidden = new JList(AttackColumnBuilder.getNamesFromInts(hidden));
            jlShown = new JList(AttackColumnBuilder.getNamesFromInts(shown));
            jlLabels = new JLabel[2];
            jlLabels[0] = new JLabel("Hidden Columns");
            jlLabels[1] = new JLabel("Visible Columns");

            jlHidden.addMouseListener(this);
            jlShown.addMouseListener(this);

            jspHidden = new JScrollPane(jlHidden);
            jspShown = new JScrollPane(jlShown);

            jpParts[0] = new JPanel(new BorderLayout());
            jpParts[0].add(jlLabels[0], BorderLayout.NORTH);
            jpParts[0].add(jspHidden, BorderLayout.CENTER);

            jpParts[1] = new JPanel(new BorderLayout());
            jpParts[1].add(jlLabels[1], BorderLayout.NORTH);
            jpParts[1].add(jspShown, BorderLayout.CENTER);

            jlHidden.setMinimumSize(new Dimension(100, 200));
            jlHidden.setPreferredSize(new Dimension(175, 250));
            jlShown.setMinimumSize(new Dimension(100, 200));
            jlShown.setPreferredSize(new Dimension(175, 250));

            jpParts[2] = new JPanel(new BorderLayout());
            jbButtons = new JButton[2];
            jbButtons[0] = new JButton("OK");
            jbButtons[1] = new JButton("Cancel");
            for (int i = 0; i < jbButtons.length; i++)
            {
                jbButtons[i].addActionListener(this);
            }
            jpParts[2].add(jbButtons[0], BorderLayout.WEST);
            jpParts[2].add(jbButtons[1], BorderLayout.EAST);

            getContentPane().removeAll();
            getContentPane().setLayout(new BorderLayout());
            getContentPane().add(jpParts[0], BorderLayout.WEST);
            getContentPane().add(jpParts[1], BorderLayout.EAST);
            getContentPane().add(jpParts[2], BorderLayout.SOUTH);
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out
                    .println("Exception creating column configuration:\t" + e);
        }
        setSize(400, 300);
        setVisible(true);
    }

    /**
     * This method determines whether or not the click is a "double click".
     */
    private boolean doubleClicked()
    {
        Date now = new Date();
        if (lastClick == null)
        {
            lastClick = new Date();
            return false;
        }
        long ms = now.getTime() - lastClick.getTime();
        lastClick = now;
        if (ms <= 500)
        {
            return true;
        }
        return false;
    }

    /**
     *  
     */
    public void showColumns()
    {
        for (int i = 0; i < jlHidden.getModel().getSize(); i++)
        {
            if (jlHidden.isSelectedIndex(i))
            {
                shown.add(new Integer(AttackColumnBuilder
                        .getIntFromName(jlHidden.getModel().getElementAt(i)
                                .toString())));
                hidden.removeElementAt(i);
            }
        }
        jlShown.setListData(AttackColumnBuilder.getNamesFromInts(shown));
        jlHidden.setListData(AttackColumnBuilder.getNamesFromInts(hidden));
    }

    /**
     *  
     */
    public void hideColumns()
    {
        for (int i = 0; i < jlShown.getModel().getSize(); i++)
        {
            if (jlShown.isSelectedIndex(i))
            {
                hidden.add(new Integer(AttackColumnBuilder
                        .getIntFromName(jlShown.getModel().getElementAt(i)
                                .toString())));
                shown.removeElementAt(i);
            }
        }
        jlShown.setListData(AttackColumnBuilder.getNamesFromInts(shown));
        jlHidden.setListData(AttackColumnBuilder.getNamesFromInts(hidden));
    }

    public void mouseClicked(MouseEvent e)
    {
        if (e.isPopupTrigger() || e.isMetaDown())
        {

        } else if (!doubleClicked())
        {
            return;
        }
        lastClick = null;
        if (e.getSource().equals(jlHidden))
        {
            showColumns();
        } else if (e.getSource().equals(jlShown))
        {
            hideColumns();
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource().equals(jbButtons[0]))
        { //OK Button
            try
            {
                pm.setValue("AttackColumns", AttackColumnBuilder
                        .intVectorToString(shown));
            } catch (Exception ex)
            {
                ex.printStackTrace();
                System.out.println("Exception setting Attack Columns:\t" + ex);
            }
            dispose();
        } else if (e.getSource().equals(jbButtons[1]))
        { //Cancel Button
            dispose();
        }
    }

    public void mouseEntered(MouseEvent e)
    {
    }

    public void mouseExited(MouseEvent e)
    {
    }

    public void mousePressed(MouseEvent e)
    {
    }

    public void mouseReleased(MouseEvent e)
    {
    }
}

