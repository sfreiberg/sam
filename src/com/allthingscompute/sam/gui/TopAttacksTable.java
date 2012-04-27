package com.allthingscompute.sam.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;

import com.allthingscompute.sam.gui.Reports.ThreadedRelatedAttackReport;
import com.allthingscompute.sam.monitor.Monitor;
import com.allthingscompute.sam.monitor.TimedMonitor;

/**
 * This class displays the top attacks (in the past 24 hours), ranked by count
 * of each type of attack. <BR>
 * <BR>
 * Sample Depiction: <BR>
 * <IMG SRC="../TopAttacksTable.png" BORDER="0"> <BR>
 * The TopAttacksTable is highlighted in Red.
 * 
 * @author sfreiberg, Documented by E. Internicola <BR>
 *         Date Jul 3, 2002
 */
public class TopAttacksTable extends MonitorTable implements ActionListener
{
    /** */
    JPopupMenu popupMenu = null;

    /** */
    JMenuItem[] menuItem = new JMenuItem[2];

    /** Column Titles. */
    private static String[] colNames = { "Type", "Count" };

    /**
     * Default Constructor - initializes the column titles, and sets the default
     * values.
     */
    public TopAttacksTable()
    {
        super(colNames);
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        popupMenu = new JPopupMenu();
        menuItem[0] = new JMenuItem("Show related attacks - last 24 hours");
        menuItem[1] = new JMenuItem("Show related attacks - last hour");

        for (int i = 0; i < 2; i++)
        {
            menuItem[i].addActionListener(this);
            popupMenu.add(menuItem[i]);
            if (i % 2 == 1)
            {
                popupMenu.addSeparator();
            }
        }

        table.addMouseListener(new MouseAdapter()
        {
            public void mouseReleased(MouseEvent event)
            {
                if (event.isPopupTrigger() || event.isMetaDown())
                {
                    popupMenu.show(event.getComponent(), event.getX(), event
                            .getY());
                }
            }

            public void mouseClicked(MouseEvent event)
            {
                if (event.isPopupTrigger() || event.isMetaDown())
                {
                    popupMenu.show(event.getComponent(), event.getX(), event
                            .getY());
                }
            }
        });
    }

    /**
     * This method is used to update the Table when a monitor changes.
     */
    public void monitorChanged(Monitor monitor)
    {
        TimedMonitor tMonitor = (TimedMonitor) monitor;
        setTableData(tMonitor.getTopAttacks());
    }

    /**
     * Actionlistener - awaits a click on a popup menu item.
     */
    public void actionPerformed(ActionEvent e)
    {
        ThreadedRelatedAttackReport trap = null;
        Object o = e.getSource();

        if (o == menuItem[0])
        {
            trap = new ThreadedRelatedAttackReport(getSelectedRow(), 24);
        } else if (o == menuItem[1])
        {
            trap = new ThreadedRelatedAttackReport(getSelectedRow(), 1);
        }
    }

    /**
     * Returns the text of the (first) selected row.
     */
    private String getSelectedRow()
    {
        int[] rows = table.getSelectedRows();
        if (rows.length == 0)
        {
            JOptionPane.showMessageDialog(table,
                    "You must select at least one row.", "Error!",
                    JOptionPane.ERROR_MESSAGE);
        } else
        {
            return (String) table.getValueAt(rows[0], 0);
        }
        return null;
    }
}