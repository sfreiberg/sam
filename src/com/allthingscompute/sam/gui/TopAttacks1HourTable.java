package com.allthingscompute.sam.gui;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.InetAddress;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import javax.swing.table.JTableHeader;

import com.allthingscompute.sam.alert.AlertInfo;
import com.allthingscompute.sam.database.EventLookup;
import com.allthingscompute.sam.database.Filter.AttackFilter;
import com.allthingscompute.sam.database.Filter.AttackFilterVector;
import com.allthingscompute.sam.database.Objects.Attack;
import com.allthingscompute.sam.database.Objects.AttackVector;
import com.allthingscompute.sam.database.Objects.AttackVectorFilter;
import com.allthingscompute.sam.gui.Dialogs.AddressInfoPanel;
import com.allthingscompute.sam.gui.Dialogs.AlertDetailPanel;
import com.allthingscompute.sam.gui.Dialogs.ColumnConfiguration;
import com.allthingscompute.sam.gui.Dialogs.DataFrame;
import com.allthingscompute.sam.gui.Reports.ThreadedRelatedAttackReport;
import com.allthingscompute.sam.gui.Tools.CSVExport;
import com.allthingscompute.sam.gui.Tools.MenuBuilder;
import com.allthingscompute.sam.gui.Tools.SortTracker;
import com.allthingscompute.sam.log.Logger;
import com.allthingscompute.sam.monitor.Monitor;
import com.allthingscompute.sam.monitor.TimedMonitor;

/**
 * This class is responsible for displaying the most recent (in the past hour)
 * attacks as reported by the snort database.
 * 
 * @author sfreiberg, Documented by E. Internicola, Modified by E. Internicola
 *         <BR>
 *         Date Jul 18, 2002
 */
public class TopAttacks1HourTable extends MonitorTable implements
        ActionListener
{
    private AttackFilterVector afv = new AttackFilterVector();

    private AttackVectorFilter avf = new AttackVectorFilter();

    private AttackVector visibleAV = null;

    private AttackVector av = null;

    private JPopupMenu popupMenu = null;

    private JMenu filterPopupMenu = null;

    private JMenu sortPopupMenu = null;

    private JMenuItem[] menuItem = null;

    private JMenuItem[] filterMenu = null;

    private JMenuItem[] sortMenu = null;

    //private static int []colNames =
    // AttackColumnBuilder.getAllColumnsAsInts();
    private static final int SHOW_SOURCE = 0;

    private static final int SHOW_DEST = 1;

    private static final int MENU_SIZE = MenuBuilder.ATTACK_MENU_SIZE;

    private static final int FILTER_MENU_SIZE = MenuBuilder.FILTER_MENU_SIZE;

    private static final int SORT_MENU_SIZE = MenuBuilder.SORT_MENU_SIZE;

    private Frame parent = null;

    private SortTracker st = new SortTracker();

    private Monitor monitor = null;

    //private AttackComparator ac = new AttackComparator(
    // AttackComparator.SORT_TIMESTAMP, true );

    public void setParent(Frame parent)
    {
        this.parent = parent;
    }

    /**
     * This is the constructor for the TopAttacks1HourTable, it sets up all the
     * column headings and sets up the event listeners and what not to handle
     * user events, and monitor update events.
     */
    public TopAttacks1HourTable()
    {
        super();
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Create popup menus
        menuItem = MenuBuilder.getAttackMenu();
        popupMenu = new JPopupMenu();

        for (int i = 0; i < MENU_SIZE; i++)
        {
            menuItem[i].addActionListener(this);
            popupMenu.add(menuItem[i]);
            if (i % 2 == 1)
            {
                popupMenu.addSeparator();
            }
        }

        filterMenu = MenuBuilder.getFilterMenu();

        filterPopupMenu = new JMenu("Filters");
        for (int i = 0; i < FILTER_MENU_SIZE; i++)
        {
            filterMenu[i].addActionListener(this);
            filterPopupMenu.add(filterMenu[i]);
            if (i % 2 == 1)
            {
                filterPopupMenu.addSeparator();
            }
        }
        popupMenu.addSeparator();
        popupMenu.add(filterPopupMenu);

        table.getTableHeader().addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent event)
            {
                if (event.isPopupTrigger() || event.isMetaDown())
                {
                    popupMenu.show(event.getComponent(), event.getX(), event
                            .getY());
                } else
                {
                    sortAttacks(event);
                }
            }

        });
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
        this.getTable().add(popupMenu);
    }

    /**
     * This method is fired whenever a monitor is updated. It will update the
     * table with the new data.
     */
    public void monitorChanged(Monitor monitor)
    {
        this.monitor = monitor;
        TimedMonitor tMonitor = (TimedMonitor) monitor;
        av = tMonitor.getAttackVector();
        av.lookupAllHosts();
        resetTable();
    }

    /**
     * This method will retreive the Currently Selected Attack.
     */
    private Attack getSelectedAttack()
    {
        Attack a = null;
        int[] rows = table.getSelectedRows();
        if (rows.length > 0)
        {
            a = (Attack) visibleAV.elementAt(rows[0]);
        }
        return a;
    }

    private void updateMonitor()
    {
        monitor.fireMonitorChanged(monitor);
    }

    /**
     * This method is responsible for determining which field to sort by, and
     * handing off the sorting to the {@link #sortAttacks(String) sortAttacks}
     * method.
     */
    private void sortAttacks(MouseEvent event)
    {
        Object source = event.getSource();
        JTableHeader jth = (JTableHeader) source;
        int col = jth.getColumnModel().getColumnIndexAtX(event.getX());
        sortAttacks(colNames[col]);
    }

    /**
     * This method does the actual sorting based on the depicted column.
     */
    private void sortAttacks(int col)
    {
        st.sortAttacks(col);
        resetTable();
    }

    private void resetTable()
    {
        avf.applyFilters(av, afv);
        visibleAV = avf.getVisibleAttackVector();
        visibleAV.sort(st.getAttackComparator());
        setTableData(visibleAV);
    }

    /**
     * This method listens for user events.
     */
    public void actionPerformed(ActionEvent event)
    {
        Object o = event.getSource();

        if (o == menuItem[MenuBuilder.ATTACK_MENU_REMOVE_FILTERS])
        {
            this.afv.clear();
            resetTable();
        } else if (o == menuItem[MenuBuilder.ATTACK_MENU_CSV_EXPORT])
        {
            CSVExport csve = new CSVExport(table);
            csve.promptFile();
        } else if (o == menuItem[MenuBuilder.ATTACK_MENU_SHOW_HIDE_COLUMNS])
        {
            ColumnConfiguration cc = new ColumnConfiguration(this.parent, true);
            reloadColumns();
            resetTable();
        } else if (o == menuItem[MenuBuilder.ATTACK_MENU_LOOKUP_ALL])
        {
            Thread t = new Thread()
            {
                public void run()
                {
                    visibleAV.forceLookupAllHosts();
                    updateMonitor();
                }
            };
            t.start();
        } else if (table.getSelectedRowCount() > 0)
        {
            if (o == menuItem[0])
            {
                displayFullDetails();
            } else if (o == menuItem[MenuBuilder.ATTACK_MENU_SHOW_DATA])
            {
                displayData();
            } else if (o == menuItem[MenuBuilder.ATTACK_MENU_SHOW_SOURCE_HOSTNAME])
            {
                displayHostname(SHOW_SOURCE);
            } else if (o == menuItem[MenuBuilder.ATTACK_MENU_SHOW_DEST_HOSTNAME])
            {
                displayHostname(SHOW_DEST);
            } else if (o == menuItem[MenuBuilder.ATTACK_MENU_RELATED_ATTACKS_24])
            {
                ThreadedRelatedAttackReport.showRelatedAttacks(getAttackType(),
                        24);
            } else if (o == menuItem[MenuBuilder.ATTACK_MENU_RELATED_ATTACKS_1])
            {
                ThreadedRelatedAttackReport.showRelatedAttacks(getAttackType(),
                        1);
            } else if (o == menuItem[MenuBuilder.ATTACK_MENU_RELATED_SOURCE_24])
            {
                ThreadedRelatedAttackReport.showRelatedSource(getSource(), 24);
            } else if (o == menuItem[MenuBuilder.ATTACK_MENU_RELATED_SOURCE_1])
            {
                ThreadedRelatedAttackReport.showRelatedSource(getSource(), 1);
            } else if (o == menuItem[MenuBuilder.ATTACK_MENU_RELATED_DEST_24])
            {
                ThreadedRelatedAttackReport.showRelatedDest(getDestination(),
                        24);
            } else if (o == menuItem[MenuBuilder.ATTACK_MENU_RELATED_DEST_1])
            {
                ThreadedRelatedAttackReport
                        .showRelatedDest(getDestination(), 1);
            } else if (o == filterMenu[0])
            { // Show - Sensor Interface
                this.afv.add(new AttackFilter(
                        AttackFilter.FIELD_SENSOR_INTERFACE,
                        getSelectedAttack().getSensor().getInterface(),
                        AttackFilter.FILTER_SHOW));
                resetTable();
            } else if (o == filterMenu[1])
            { // Hide - Sensor Interface
                this.afv.add(new AttackFilter(
                        AttackFilter.FIELD_SENSOR_INTERFACE,
                        getSelectedAttack().getSensor().getInterface(),
                        AttackFilter.FILTER_HIDE));
                resetTable();
            } else if (o == filterMenu[2])
            { // Show - Sensor IP
                this.afv.add(new AttackFilter(
                        AttackFilter.FIELD_SENSOR_ADDRESS, getSelectedAttack()
                                .getSensor().getHost().getIP(),
                        AttackFilter.FILTER_SHOW));
                resetTable();
            } else if (o == filterMenu[3])
            { // Hide - Sensor IP
                this.afv.add(new AttackFilter(
                        AttackFilter.FIELD_SENSOR_ADDRESS, getSelectedAttack()
                                .getSensor().getHost().getIP(),
                        AttackFilter.FILTER_HIDE));
                resetTable();
            } else if (o == filterMenu[4])
            { // Show - Source Address
                this.afv.add(new AttackFilter(AttackFilter.FIELD_SOURCE,
                        getSelectedAttack().getSource().toString(),
                        AttackFilter.FILTER_SHOW));
                resetTable();
            } else if (o == filterMenu[5])
            { // Hide - Source Address
                this.afv.add(new AttackFilter(AttackFilter.FIELD_SOURCE,
                        getSelectedAttack().getSource().toString(),
                        AttackFilter.FILTER_HIDE));
                resetTable();
            } else if (o == filterMenu[6])
            { // Show - Destination Address
                this.afv.add(new AttackFilter(AttackFilter.FIELD_DESTINATION,
                        getSelectedAttack().getSource().toString(),
                        AttackFilter.FILTER_SHOW));
                resetTable();
            } else if (o == filterMenu[7])
            { // Hide - Destination Address
                this.afv.add(new AttackFilter(AttackFilter.FIELD_DESTINATION,
                        getSelectedAttack().getSource().toString(),
                        AttackFilter.FILTER_HIDE));
                resetTable();
            } else if (o == filterMenu[8])
            { // Show - Attack Signature
                this.afv.add(new AttackFilter(
                        AttackFilter.FIELD_ATTACK_SIGNATURE,
                        getSelectedAttack().getSignature().getSigName(),
                        AttackFilter.FILTER_SHOW));
                resetTable();
            } else if (o == filterMenu[9])
            { // Hide - Attack Signature
                this.afv.add(new AttackFilter(
                        AttackFilter.FIELD_ATTACK_SIGNATURE,
                        getSelectedAttack().getSignature().getSigName(),
                        AttackFilter.FILTER_HIDE));
                resetTable();
            }
        } else
        {
            JOptionPane.showMessageDialog(table,
                    "You must select at least one row.", "User Error!",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method will retreive the "Source" field of the given selected table
     * element.
     */
    private String getSource()
    {
        Attack a = getSelectedAttack();
        if (a != null)
        {
            Long l = new Long(a.getSource().getIPHash());
            return l.toString();
        }
        return null;
    }

    /**
     * This method will retreive the "Destination" field of the given selected
     * table element.
     */
    private String getDestination()
    {
        Attack a = getSelectedAttack();
        if (a != null)
        {
            Long l = new Long(a.getDestination().getIPHash());
            return l.toString();
        }
        return null;
    }

    /**
     * This method will retreive the "Attack Type" (or Signature) of the given
     * selected table element.
     */
    private String getAttackType()
    {
        Attack a = getSelectedAttack();
        if (a != null)
        {
            return a.getSignature().getSigName();
        }
        return null;
    }

    /**
     * This method will display the data for the given selected attack.
     */
    public void displayData()
    {
        try
        {
            Attack a = getSelectedAttack();
            DataFrame df = new DataFrame(a.getSid().longValue(), a.getCid()
                    .longValue());
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Exception displaying data portion");
        }
    }

    /**
     * Display's more information about a row from the list.
     */
    public void displayHostname(int whichAddress)
    {
        Attack a = getSelectedAttack();
        InetAddress address = null;
        if (whichAddress == SHOW_DEST)
        {
            a.getDestination().executeLookup();
            address = a.getDestination().getInetAddress();
        } else if (whichAddress == SHOW_SOURCE)
        {
            a.getSource().executeLookup();
            address = a.getSource().getInetAddress();
        }
        try
        {
            JFrame frame = new JFrame("Hostname");
            frame.getContentPane().add(new AddressInfoPanel(address));
            frame.pack();
            frame.setVisible(true);
        } catch (Exception e)
        {
            JOptionPane.showMessageDialog(table, e.getMessage(), "Error!",
                    JOptionPane.ERROR_MESSAGE);
        }
        updateMonitor();
    }

    /**
     * Display full details for selected alerts.
     */
    public void displayFullDetails()
    {
        Attack a = getSelectedAttack();
        String alertId = a.getCid().toString();
        a.getSource().executeLookup();
        a.getDestination().executeLookup();
        String sourceAddress = a.getSource().toString();
        String destAddress = a.getDestination().toString();
        EventLookup el = new EventLookup(a.getCid().toString());
        String sourcePort = el.getSPort();
        String destPort = el.getDPort();
        AlertInfo alertInfo = null;
        try
        {
            alertInfo = new AlertInfo(alertId, sourceAddress, destAddress,
                    sourcePort, destPort);
        } catch (Exception e)
        {
            Logger.writeToLog(e.getMessage());
        }
        JFrame frame = new JFrame("Alert Details");
        frame.getContentPane().add(new AlertDetailPanel(alertInfo));
        frame.pack();
        frame.setVisible(true);
        updateMonitor();
    }
}