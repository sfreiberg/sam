package com.allthingscompute.sam.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;

import com.allthingscompute.sam.database.Objects.TopAttacker;
import com.allthingscompute.sam.database.Objects.TopAttackerVector;
import com.allthingscompute.sam.gui.Dialogs.AddressInfoPanel;
import com.allthingscompute.sam.gui.Reports.ThreadedRelatedAttackReport;
import com.allthingscompute.sam.monitor.Monitor;
import com.allthingscompute.sam.monitor.TimedMonitor;
import com.allthingscompute.sam.net.Lookup;

/**
 * This class displays the top Attackers in the past 24 hours, ordered by their
 * count. It also listens for mouse events (right clicking) on the table. <BR>
 * <BR>
 * Sample Depiction: <BR>
 * <IMG SRC="../TopAttackersTable.png" BORDER="0"> <BR>
 * The Top Attackers Table is highlighted in Red
 * 
 * @author sfreiberg, Documented by E. Internicola <BR>
 *         Date Jul 3, 2002
 */
public class TopAttackersTable extends MonitorTable implements ActionListener
{
    private JPopupMenu popupMenu = null;

    private JMenuItem[] menuItem = new JMenuItem[4];

    private TopAttackerVector tav = null;

    private Monitor monitor = null;

    private static String[] colNames = { "Attacker", "Count" };

    /**
     * Default Constructor - this sets up all the visual and functional
     * attributes of the TopAttackersTable object. It also sets up the popup
     * menu, and configures the table to listen for right click mouse events.
     */
    public TopAttackersTable()
    {
        super(colNames);
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        popupMenu = new JPopupMenu();

        menuItem = new JMenuItem[4];

        menuItem[0] = new JMenuItem("Show Hostname");
        menuItem[1] = new JMenuItem("Force Host Lookup");
        menuItem[2] = new JMenuItem("Related Source Attacks - Last 24 hours");
        menuItem[3] = new JMenuItem("Related Source Attacks - Last hour");

        for (int i = 0; i < 4; i++)
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

    private void updateMonitor()
    {
        monitor.fireMonitorChanged(monitor);
    }

    /**
     * This method will update the table when the monitor changes.
     */
    public void monitorChanged(Monitor monitor)
    {
        this.monitor = monitor;
        TimedMonitor tMonitor = (TimedMonitor) monitor;
        //setTableData( tMonitor.getTopAttackers() );
        tav = tMonitor.getTopAttackerVector();
        tav.lookupAllHosts();
        setTableData(tav);
    }

    /**
     *  
     */
    private void setTableData(TopAttackerVector tav)
    {
        Vector data = new Vector();
        for (int i = 0; i < tav.size(); i++)
        {
            TopAttacker ta = (TopAttacker) tav.elementAt(i);
            Vector v = new Vector(2);
            v.add(ta.getHost().getCanonical());
            v.add(ta.getCount());
            data.add(v);
        }
        setTableData(data);
        table.validate();
    }

    /**
     * This method listens for the event that the user clicks on the Popup Menu
     * Item. When they do it takes the appropriate action.
     */
    public void actionPerformed(ActionEvent event)
    {
        Object o = event.getSource();
        if (o == menuItem[0])
        {
            displayHostname(0);
        } else if (o == menuItem[1])
        {
            Thread t = new Thread()
            {
                public void run()
                {
                    tav.forceLookupAllHosts();
                    updateMonitor();
                }
            };
            t.start();

        } else if (o == menuItem[2])
        {
            ThreadedRelatedAttackReport.showRelatedSource(getAttacker()
                    .getIPHash(), 24);
        } else if (o == menuItem[3])
        {
            ThreadedRelatedAttackReport.showRelatedSource(getAttacker()
                    .getIPHash(), 1);
        }
    }

    /**
     * This method will retreive the "Attacker" that is currently selected in
     * the list.
     */
    private Lookup getAttacker()
    {
        return getSelectedAttacker().getHost();
    }

    /**
     * This method returns the Selected Attacker.
     */
    private TopAttacker getSelectedAttacker()
    {
        try
        {
            int[] rows = table.getSelectedRows();
            return (TopAttacker) tav.elementAt(rows[0]);
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * This method will display Host information (when the users clicks on the
     * the Popup Menu Item). It makes use of the
     * {@link AddressInfoPanel AddressInfoPanel}object.
     */
    public void displayHostname(int column)
    {
        TopAttacker ta = getSelectedAttacker();
        if (ta != null)
        {
            try
            {
                ta.getHost().executeLookup();
                JFrame frame = new JFrame("Hostname");
                frame.getContentPane().add(
                        new AddressInfoPanel(ta.getHost().getCanonical(), ta
                                .getHost().getIP()));
                frame.pack();
                frame.setVisible(true);
            } catch (Exception e)
            {
                JOptionPane.showMessageDialog(table, e.getMessage(), "Error!",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        updateMonitor();
    }
}