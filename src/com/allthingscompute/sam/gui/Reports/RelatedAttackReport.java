/*
 *  RelatedAttackReport.java
 *  
 *  Created on February 5, 2004, 1:59 PM
 */
package com.allthingscompute.sam.gui.Reports;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.InetAddress;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.JTableHeader;

import com.allthingscompute.sam.alert.AlertInfo;
import com.allthingscompute.sam.database.EventLookup;
import com.allthingscompute.sam.database.Filter.AttackFilter;
import com.allthingscompute.sam.database.Filter.AttackFilterVector;
import com.allthingscompute.sam.database.Objects.Attack;
import com.allthingscompute.sam.database.Objects.AttackVector;
import com.allthingscompute.sam.database.Objects.AttackVectorFilter;
import com.allthingscompute.sam.database.Report.ReportEngine;
import com.allthingscompute.sam.gui.MonitorTable;
import com.allthingscompute.sam.gui.Dialogs.AddressInfoPanel;
import com.allthingscompute.sam.gui.Dialogs.AlertDetailPanel;
import com.allthingscompute.sam.gui.Dialogs.ColumnConfiguration;
import com.allthingscompute.sam.gui.Dialogs.DataFrame;
import com.allthingscompute.sam.gui.Tools.AttackColumnBuilder;
import com.allthingscompute.sam.gui.Tools.CSVExport;
import com.allthingscompute.sam.gui.Tools.MenuBuilder;
import com.allthingscompute.sam.gui.Tools.SortTracker;
import com.allthingscompute.sam.log.Logger;

/**
 * This class is used to display related attacks, by Attack Type, Source, and
 * Destination.
 * 
 * @author E. Internicola
 * @see com.allthingscompute.sam.database.DatabaseInterface.getRelatedAttacks
 */
public class RelatedAttackReport extends JFrame implements ActionListener,
        WindowListener, ComponentListener
{
    public static final int RELATED_SOURCE = 0;

    public static final int RELATED_DEST = 1;

    private static final int MENU_SIZE = MenuBuilder.ATTACK_MENU_SIZE;

    private static final int FILTER_MENU_SIZE = MenuBuilder.FILTER_MENU_SIZE;

    private AttackFilterVector afv = new AttackFilterVector();

    private AttackVectorFilter avf = new AttackVectorFilter();

    private JPopupMenu popupMenu = null;

    private JMenu filterPopupMenu = null;

    private JMenuItem[] menuItems = null;

    private JMenuItem[] filterMenu = null;

    private String AttackType = null;

    //private ReportEngine re = null;
    private AttackVector av = null;

    private AttackVector visibleAV = null;

    //private AttackComparator ac = new AttackComparator(
    // AttackComparator.SORT_TIMESTAMP, true );
    private SortTracker st = new SortTracker();

    private static int[] colNames = AttackColumnBuilder.getAllColumnsAsInts();

    private MonitorTable mt = null;

    private JScrollPane tableScroll = null;

    /**
     * This method will show the related attacks to a given attack type.
     */
    protected static void showRelatedAttacks(String attackType, int hours)
    {
        RelatedAttackReport rar = new RelatedAttackReport(attackType, hours);
    }

    /**
     * This method will show a related source
     */
    protected static void showRelatedSource(String source, int hours)
    {
        RelatedAttackReport rar = new RelatedAttackReport(getIPHash(source),
                hours, RelatedAttackReport.RELATED_SOURCE);
    }

    /**
     * This method will show a related destination attack table (in a new
     * window); where the attackers are all the same as dest. input parameter:
     * dest the ip, or hostname of the destination.
     */
    protected static void showRelatedDest(String dest, int hours)
    {
        RelatedAttackReport rar = new RelatedAttackReport(getIPHash(dest),
                hours, RelatedAttackReport.RELATED_DEST);
    }

    /**
     * This method will take a host name as a string, and attempt to create an
     * instance of an InetAddress object from it (because a canonical name might
     * be supplied), then convert its IP to a long value. The
     * InetAddress.hashCode method fails here because it overflows into a
     * negative value, so we need a long to hold the entire value. Perhaps there
     * is some way to convert an int to a long and account for the overflow, but
     * I'm unaware of it thus far, so I used a little bit of mathematics to do
     * the conversion.
     */
    private static long getIPHash(String host)
    {
        try
        {
            InetAddress ia = InetAddress.getByName(host);
            String[] parts = ia.getHostAddress().split("\\.");
            long value = 0;
            long part = Long.parseLong(parts[0]);
            value += part * (long) Math.pow((double) 256, (double) 3);
            part = Long.parseLong(parts[1]);
            value += part * (long) Math.pow((double) 256, (double) 2);
            part = Long.parseLong(parts[2]);
            value += part * 256;
            part = Long.parseLong(parts[3]);
            value += part;
            return value;
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Exception getting the IP Hash");
        }
        return -1;
    }

    /**
     * This method will create the JMenu (and the JMenuItems it contains).
     */
    private void createObjects()
    {
        // Create popup menus
        menuItems = MenuBuilder.getAttackMenu();
        popupMenu = new JPopupMenu();

        for (int i = 0; i < MENU_SIZE; i++)
        {
            menuItems[i].addActionListener(this);
            popupMenu.add(menuItems[i]);
            if (i % 2 != 0)
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
        avf = new AttackVectorFilter();
    }

    /**
     * This method is used to setup the table.
     */
    private void setupTable()
    {
        this.getContentPane().setLayout(new FlowLayout());
        filterAttacks();
        mt = new MonitorTable();
        mt.setTableData(visibleAV);
        mt.getTable().getTableHeader().setReorderingAllowed(false);
        mt.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mt.getTable().repaint();
        tableScroll = new JScrollPane(mt.getTable(),
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    private void filterAttacks()
    {
        avf.applyFilters(av, afv);
        visibleAV = avf.getVisibleAttackVector();
        visibleAV.sort(st.getAttackComparator());
    }

    private void resetTable()
    {
        filterAttacks();
        mt.setTableData(visibleAV);
        tableScroll.setPreferredSize(new Dimension(getWidth() - 20,
                getHeight() - 40));
        this.validate();
    }

    /**
     *  
     */
    private void finalizeFrame()
    {
        mt.getTable().addMouseListener(new MouseAdapter()
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
        mt.getTable().getTableHeader().addMouseListener(new MouseAdapter()
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
        this.getContentPane().add(tableScroll);
        this.getContentPane().addComponentListener(this);
    }

    /**
     *  
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

    /**
     * This Constructor takes an AttackVector, and displays it.
     */
    protected RelatedAttackReport(AttackVector av)
    {
        this.av = av;
        createObjects();
        setupTable();
        finalizeFrame();
    }

    /**
     * This constructor is used to create a "Related Source" or "Related
     * Destination" attack frame. ipAddr is the integer value (hashCode) of the
     * source or destination for the attack report. hours is the number of hours
     * to look back (using the current date and time). The value for "type" is
     * either {@link #RELATED_SOURCE RELATED_SOURCE}or
     * {@link #RELATED_DEST RELATED_DEST}
     */
    protected RelatedAttackReport(long ipAddr, int hours, int type)
    {
        super("Related Reports");
        createObjects();
        setReportType(ipAddr, hours, type);
        setupTable();
        //tableScroll.setBorder( new TitledBorder( "Related Attacks (Last hrs)"
        // ) );
        finalizeFrame();
        pack();
        setVisible(true);
    }

    /**
     * This constructor creates a new "Related Attack Type" frame based on the
     * "attackType" and number of hours to look back (using the current date and
     * time).
     */
    protected RelatedAttackReport(String attackType, int hours)
    {
        super("Related Attacks (Last " + hours + " hrs)");
        createObjects();
        setReportType(attackType, hours);
        setupTable();
        tableScroll.setBorder(new TitledBorder("Related Attacks -'"
                + attackType + "' (Last " + hours + " hrs)"));
        finalizeFrame();
        pack();
        setVisible(true);
    }

    /**
     * This method is used by the constructor to initialize the frame for either
     * a related source, or destination report. This method will retreive the
     * result set using helper objects and methods.
     * {@see com.allthingscompute.sam.database.Report.ReportEngine}
     */
    private void setReportType(long ipAddr, int hours, int type)
    {
        try
        {
            //re = new ReportEngine();
            if (type == RELATED_DEST)
            {
                av = ReportEngine.getRelatedAttacksByDestination(ipAddr, hours);
            } else if (type == RELATED_SOURCE)
            {
                av = ReportEngine.getRelatedAttacksBySource(ipAddr, hours);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Exception Setting Report Type:\t" + e);
        }
    }

    /** Sets up the report type. */
    public void setReportType(String attackType, int hours)
    {
        try
        {
            AttackType = attackType;
            //re = new ReportEngine();
            av = ReportEngine.getRelatedAttacks(attackType, hours);
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Exception Setting Report Type:\t" + e);
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        Object o = e.getSource();
        if (o == menuItems[MenuBuilder.ATTACK_MENU_CSV_EXPORT])
        {
            CSVExport csve = new CSVExport(mt.getTable());
            csve.promptFile();
        } else if (o == menuItems[MenuBuilder.ATTACK_MENU_REMOVE_FILTERS])
        {
            this.afv.clear();
            resetTable();
        } else if (o == menuItems[MenuBuilder.ATTACK_MENU_SHOW_HIDE_COLUMNS])
        {
            ColumnConfiguration cc = new ColumnConfiguration(this, true);
            mt.reloadColumns();
            resetTable();
        } else if (o == menuItems[MenuBuilder.ATTACK_MENU_LOOKUP_ALL])
        {
            Thread t = new Thread()
            {
                public void run()
                {
                    visibleAV.forceLookupAllHosts();
                    resetTable();
                }
            };
            t.start();
        } else if (mt.getTable().getSelectedRowCount() > 0)
        {
            if (o == menuItems[MenuBuilder.ATTACK_MENU_SHOW_DETAILS])
            {
                displayFullDetails();
            } else if (o == menuItems[MenuBuilder.ATTACK_MENU_SHOW_DATA])
            {
                displayData();
            } else if (o == menuItems[MenuBuilder.ATTACK_MENU_SHOW_SOURCE_HOSTNAME])
            {
                displayHostname(AttackFilter.FIELD_SOURCE);
            } else if (o == menuItems[MenuBuilder.ATTACK_MENU_SHOW_DEST_HOSTNAME])
            {
                displayHostname(AttackFilter.FIELD_DESTINATION);
            } else if (o == menuItems[MenuBuilder.ATTACK_MENU_RELATED_ATTACKS_24])
            {
                RelatedAttackReport.showRelatedAttacks(getAttackType(), 24);
            } else if (o == menuItems[MenuBuilder.ATTACK_MENU_RELATED_ATTACKS_1])
            {
                RelatedAttackReport.showRelatedAttacks(getAttackType(), 1);
            } else if (o == menuItems[MenuBuilder.ATTACK_MENU_RELATED_SOURCE_24])
            {
                RelatedAttackReport.showRelatedSource(getSource(), 24);
            } else if (o == menuItems[MenuBuilder.ATTACK_MENU_RELATED_SOURCE_1])
            {
                RelatedAttackReport.showRelatedSource(getSource(), 1);
            } else if (o == menuItems[MenuBuilder.ATTACK_MENU_RELATED_DEST_24])
            {
                RelatedAttackReport.showRelatedDest(getDestination(), 24);
            } else if (o == menuItems[MenuBuilder.ATTACK_MENU_RELATED_DEST_1])
            {
                RelatedAttackReport.showRelatedDest(getDestination(), 1);
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
            { // Show - Sensor Address
                this.afv.add(new AttackFilter(
                        AttackFilter.FIELD_SENSOR_ADDRESS, getSelectedAttack()
                                .getSensor().getHost().getIP(),
                        AttackFilter.FILTER_SHOW));
                resetTable();
            } else if (o == filterMenu[3])
            { // Hide - Sensor Address
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
            JOptionPane.showMessageDialog(this,
                    "You must select at least one row.", "User Error!",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void windowClosing(WindowEvent e)
    {
    }

    public void windowActivated(WindowEvent e)
    {
    }

    public void windowDeactivated(WindowEvent e)
    {
    }

    public void windowDeiconified(WindowEvent e)
    {
    }

    public void windowIconified(WindowEvent e)
    {
    }

    public void windowOpened(WindowEvent e)
    {
    }

    public void windowClosed(WindowEvent e)
    {
        this.dispose();
    }

    public void componentHidden(ComponentEvent e)
    {
    }

    public void componentMoved(ComponentEvent e)
    {
    }

    public void componentResized(ComponentEvent e)
    {
        resize();
    }

    public void componentShown(ComponentEvent e)
    {
        resize();
    }

    private void resize()
    {
        tableScroll.setPreferredSize(new Dimension(getWidth() - 20,
                getHeight() - 40));
    }

    /**
     * Display the data associated with the selected Record.
     */
    private void displayData()
    {
        int[] rows = mt.getTable().getSelectedRows();
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
     * This method will retreive the "Source" field of the given selected table
     * element.
     */
    private String getSource()
    {
        return getSelectedAttack().getSource().toString();
    }

    /**
     * This method will retreive the "Destination" field of the given selected
     * table element.
     */
    private String getDestination()
    {
        return getSelectedAttack().getDestination().toString();
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
     * This method will return the currently selected Attack, or null if no
     * attack is selected.
     */
    private Attack getSelectedAttack()
    {
        Attack a = null;
        int[] rows = mt.getTable().getSelectedRows();
        a = (Attack) visibleAV.elementAt(rows[0]);
        return a;
    }

    /**
     * Display's more information about a row from the list.
     */
    public void displayHostname(int host)
    {
        int[] rows = mt.getTable().getSelectedRows();
        if (rows.length == 0)
        {
            JOptionPane.showMessageDialog(mt.getTable(),
                    "You must select at least one row.", "Error!",
                    JOptionPane.ERROR_MESSAGE);
        } else
        {
            for (int i = 0; i < rows.length; i++)
            {
                InetAddress address = null;
                String addressString = null;
                Attack a = (Attack) visibleAV.elementAt(rows[0]);
                if (host == AttackFilter.FIELD_SOURCE)
                {
                    address = a.getSource().getInetAddress();
                    addressString = a.getSource().toString();
                } else if (host == AttackFilter.FIELD_DESTINATION)
                {
                    address = a.getDestination().getInetAddress();
                    addressString = a.getDestination().toString();
                }
                try
                {
                    address = InetAddress.getByName(addressString);
                    JFrame frame = new JFrame("Hostname");
                    frame.getContentPane().add(new AddressInfoPanel(address));
                    frame.pack();
                    frame.setVisible(true);
                } catch (Exception e)
                {
                    JOptionPane
                            .showMessageDialog(mt.getTable(), e.getMessage(),
                                    "Error!", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * Display full details for selected alerts.
     */
    public void displayFullDetails()
    {
        int[] rows = mt.getTable().getSelectedRows();
        if (rows.length == 0)
        {
            JOptionPane.showMessageDialog(mt.getTable(),
                    "You must select at least one row.", "Error!",
                    JOptionPane.ERROR_MESSAGE);
        } else
        {
            for (int i = 0; i < rows.length; i++)
            {
                String alertId = getSelectedAttack().getCid().toString();
                String sourceAddress = getSelectedAttack().getSource()
                        .toString();
                String destAddress = getSelectedAttack().getDestination()
                        .toString();
                EventLookup el = new EventLookup(alertId);
                String sourcePort = el.getSPort();
                String destPort = el.getDPort();
                AlertInfo alertInfo = null;
                try
                {
                    alertInfo = new AlertInfo(alertId, sourceAddress,
                            destAddress, sourcePort, destPort);
                } catch (Exception e)
                {
                    Logger.writeToLog(e.getMessage());
                }
                JFrame frame = new JFrame("Alert Details");
                frame.getContentPane().add(new AlertDetailPanel(alertInfo));
                frame.pack();
                frame.setVisible(true);
            }
        }
    }
}

