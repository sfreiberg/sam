package com.allthingscompute.sam.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowListener;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import com.allthingscompute.sam.alert.AlertLevel;
import com.allthingscompute.sam.alert.AlertLevelChangeListener;
import com.allthingscompute.sam.alert.AudioAlert;
import com.allthingscompute.sam.alert.EmailAlert;
import com.allthingscompute.sam.gui.Dialogs.AboutPanel;
import com.allthingscompute.sam.gui.Dialogs.DataPurge;
import com.allthingscompute.sam.gui.Dialogs.DatabaseLoginPanel;
import com.allthingscompute.sam.gui.Dialogs.HelpFrame;
import com.allthingscompute.sam.log.LogFileLogger;
import com.allthingscompute.sam.log.Logger;
import com.allthingscompute.sam.monitor.TimedMonitor;

/**
 * This is the Initial GUI that the user will see, and it is the interface by
 * which a user will view alerts and things related.
 * 
 * @author sfreiberg, Documented by E. Internicola <BR>
 *         Date Jun 26, 2002 <BR>
 *         Modified by E. Internicola (05/05/2003)
 * @see com.allthingscompute.sam.gui.AlertsPanel
 */
public class MainFrame extends JFrame implements ActionListener,
        AlertLevelChangeListener, ComponentListener, WindowListener
{

    // Icon to be displayed in Frame
    private ImageIcon noAlertIcon = new ImageIcon("icons/notherm.gif");

    private ImageIcon lowAlertIcon = new ImageIcon("icons/greentherm.gif");

    private ImageIcon medAlertIcon = new ImageIcon("icons/yellowtherm.gif");

    private ImageIcon highAlertIcon = new ImageIcon("icons/redtherm.gif");

    // Menu bar and items
    private JMenuBar menuBar = null;

    private JMenu fileMenu = null;

    private JMenuItem refreshMenuItem = null, quitMenuItem = null;

    private JMenu editMenu = null;

    private JMenuItem prefsMenuItem = null;

    private JMenu monitorMenu = null;

    private JMenu helpMenu = null;

    private JMenuItem helpMenuItem = null, aboutMenuItem = null;

    private JMenu toolsMenu = null;

    private JMenuItem toolsMenuItem = null;

    // Other components
    //private VisualAlert stoplightLabel = null;

    private ThermometerPanel thermometerPanel = null;
    
    private JTabbedPane tabbedPane = null;

    private TextAlertsPanel textAlert = null;

    private EmailAlert emailAlert = null;

    private AudioAlert audioAlert = null;

    private GraphPanel lastHourGraph = null;

    private TopAttacks1HourTable topAttacks1HourTable = null;

    private LoggingPanel loggingPanel = null;
    
    private PriorityGraphPanel priorityGraphPanel = null;
    
    private ProtocolGraphPanel protocolGraphPanel = null;

    // Lower status bar
    private JProgressBar progressBar = null;

    private JLabel statusBarLabel = null;

    private JPanel lowerPanel = null;

    // Monitors
    private TimedMonitor timedMonitor = null;

    private DataPurge dp = null;

    /**
     * Initialize and Display the Frame.
     */
    public MainFrame()
    {
        // Create frame and set defaults
        super("SAM");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(noAlertIcon.getImage());
        getContentPane().setLayout(new BorderLayout());

        // Create MenuBar and add items to it
        menuBar = new JMenuBar();

        fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        editMenu = new JMenu("Edit");
        editMenu.setMnemonic('E');
        monitorMenu = new JMenu("Monitor");
        monitorMenu.setMnemonic('M');
        helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');
        toolsMenu = new JMenu("Tools");
        toolsMenu.setMnemonic('T');

        refreshMenuItem = new JMenuItem("Refresh");
        refreshMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
                InputEvent.CTRL_MASK));
        refreshMenuItem.addActionListener(this);
        quitMenuItem = new JMenuItem("Quit");
        quitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
                InputEvent.CTRL_MASK));
        quitMenuItem.addActionListener(this);
        prefsMenuItem = new JMenuItem("Preferences");
        prefsMenuItem.addActionListener(this);
        helpMenuItem = new JMenuItem("Help");
        helpMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,
                InputEvent.CTRL_MASK));
        helpMenuItem.addActionListener(this);
        aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
                InputEvent.CTRL_MASK));
        aboutMenuItem.addActionListener(this);
        toolsMenuItem = new JMenuItem("Data Purge");
        toolsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,
                InputEvent.CTRL_MASK));
        toolsMenuItem.addActionListener(this);

        fileMenu.add(refreshMenuItem);
        fileMenu.add(quitMenuItem);
        editMenu.add(prefsMenuItem);
        helpMenu.add(helpMenuItem);
        helpMenu.add(aboutMenuItem);
        toolsMenu.add(toolsMenuItem);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(monitorMenu);
        menuBar.add(toolsMenu);
        menuBar.add(helpMenu);

        // Create monitor and alert level change listeners
        /* stoplightLabel = new VisualAlert("icons/stoplight_none.gif",
                "icons/stoplight_green.gif", "icons/stoplight_yellow.gif",
                "icons/stoplight_red.gif"); */
        thermometerPanel = new ThermometerPanel();
        textAlert = new TextAlertsPanel();
        try
        {
            emailAlert = new EmailAlert();
        } catch (Exception exception)
        {
            Logger.writeToLog(exception.getMessage());
        }
        audioAlert = new AudioAlert("wav/low_alert.wav", "wav/med_alert.wav",
                "wav/high_alert.wav");
        
        priorityGraphPanel = new PriorityGraphPanel();
        protocolGraphPanel = new ProtocolGraphPanel();
        
        // create tabbed pane
        lastHourGraph = new GraphPanel();
        topAttacks1HourTable = new TopAttacks1HourTable();
        topAttacks1HourTable.setParent(this);
        loggingPanel = new LoggingPanel();
        Logger.addLoggable(loggingPanel);
        Logger.addLoggable(LogFileLogger.getLogFileLogger());
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.addTab("Graphs", lastHourGraph);
        tabbedPane.addTab("Recent Attempts", new JScrollPane(
                topAttacks1HourTable.getTable(),
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
        
        Box oneDayOverview = Box.createHorizontalBox();
        TopAttacksTable attacksTable = new TopAttacksTable();
        JScrollPane tableScroll = new JScrollPane(attacksTable.getTable(),
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tableScroll.setBorder(new TitledBorder("Top Attacks  (Last 24 hrs)"));
        //upperBox.add(tableScroll);

        // Create attackers table
        TopAttackersTable attackersTable = new TopAttackersTable();
        JScrollPane tableScroll2 = new JScrollPane(attackersTable.getTable(),
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        //Dimension d = attackersTable.getTable().getPreferredSize();
        Dimension d = new Dimension();
        d.height = 150;
        d.width = 250;
        tableScroll2.setPreferredSize(d);
        tableScroll.setPreferredSize(d);
        tableScroll2
                .setBorder(new TitledBorder("Top Attackers  (Last 24 hrs)"));
        //upperBox.add(tableScroll2);
        oneDayOverview.add(tableScroll);
        oneDayOverview.add(tableScroll2);
        tabbedPane.addTab("24 hour Summary", oneDayOverview);

        tabbedPane.addTab("SAM log", new JScrollPane(loggingPanel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
        tabbedPane.addTab("Alert Priority Totals", priorityGraphPanel);
        /* tabbedPane.addTab("Watch List", new JLabel(
                "This feature is not yet implemented.")); */

        setJMenuBar(menuBar);

        Box upperBox = Box.createHorizontalBox();
        //JPanel stoplightPanel = new JPanel();
        //stoplightPanel.setBorder(new TitledBorder("Alert Status"));
        //stoplightPanel.add(thermometerPanel);
        //upperBox.add(stoplightPanel);
        //thermometerPanel.setBorder(new TitledBorder("Alerts"));
        upperBox.add(thermometerPanel);
        upperBox.add(priorityGraphPanel);
        upperBox.add(protocolGraphPanel);
        //upperBox.add(textAlert);



        Box lowerBox = Box.createVerticalBox();
        lowerBox.add(upperBox);
        lowerBox.add(tabbedPane);

        // Build the lower status bar
        progressBar = new JProgressBar(0, 10);
        progressBar.setBorder(new BevelBorder(BevelBorder.LOWERED));
        progressBar.setStringPainted(true);
        statusBarLabel = new JLabel("No Database Connection");
        lowerPanel = new JPanel(new BorderLayout());
        lowerPanel.add(statusBarLabel, BorderLayout.WEST);
        lowerPanel.add(progressBar, BorderLayout.CENTER);

        getContentPane().add(lowerBox, BorderLayout.CENTER);
        getContentPane().add(lowerPanel, BorderLayout.SOUTH);

        // Create menu items for turning off monitors
        addMonitorMenuItem(emailAlert);
        addMonitorMenuItem(audioAlert);

        setVisible(true);

        // Create the monitors
        try
        {
            //Integer iTmp = new Integer(
            // PropertiesManager.getPropertiesManager().getValue(
            // "mainpanel.refresh" ) );
            //timedMonitor = new TimedMonitor( iTmp.intValue(),
            // AlertLevel.NO_ALERT_LEVEL );
            timedMonitor = new TimedMonitor();
            timedMonitor.setProgressBar(progressBar);
        } catch (Exception exception)
        {
            Logger.writeToLog(exception.getMessage());
        }

        DatabaseLoginPanel loginPanel = DatabaseLoginPanel
                .getDatabaseLoginPanel(this);
        loginPanel.setVisible(true);

        statusBarLabel.setText("Connected to Database");

        // register listeners with monitors and start monitors
        //timedMonitor.addAlertLevelChangeListener(stoplightLabel);
        timedMonitor.addMonitorChangeListener(thermometerPanel);
        timedMonitor.addMonitorChangeListener(textAlert);
        timedMonitor.addMonitorChangeListener(attacksTable);
        timedMonitor.addMonitorChangeListener(attackersTable);
        timedMonitor.addAlertLevelChangeListener(emailAlert);
        timedMonitor.addAlertLevelChangeListener(audioAlert);
        timedMonitor.addMonitorChangeListener(lastHourGraph);
        timedMonitor.addMonitorChangeListener(topAttacks1HourTable);
        timedMonitor.addMonitorChangeListener(priorityGraphPanel);
        timedMonitor.addMonitorChangeListener(protocolGraphPanel);
        timedMonitor.addAlertLevelChangeListener(this);
        timedMonitor.start();
        addComponentListener(this);
    }

    /**
     * This adds a menu to the JFrame.
     */
    private void addMonitorMenuItem(Object listener)
    {
        JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(listener.toString());
        menuItem.setSelected(true);
        menuItem.addActionListener(this);
        monitorMenu.add(menuItem);
    }

    /**
     * This method handles menu "on-click" events. It handles events for:
     * <UL>
     * <LI>File->Refresh</LI>
     * <LI>File->Quit</LI>
     * <LI>Help->About</LI>
     * <LI>All other menus</LI>
     * </UL>
     * 
     * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
     */
    public void actionPerformed(ActionEvent e)
    {
        Object o = e.getSource();
        if (o == refreshMenuItem)
        {
            Thread t = new Thread()
            {
                public void run()
                {
                    timedMonitor.update();
                }
            };
            t.start();
        } else if (o == aboutMenuItem)
        {
            AboutPanel ap = new AboutPanel(this);
            ap.setLocationRelativeTo(this);
        } else if (o == quitMenuItem)
        {
            System.exit(0);
        } else if (o == toolsMenuItem)
        {
            dp = new DataPurge();
        } else if (o == prefsMenuItem)
        {
            new PropertiesFrame();
        } else if (o == helpMenuItem)
        {
            try
            {
                new HelpFrame();
            } catch (Exception ex)
            {
                JOptionPane.showMessageDialog(this, "Unable to display Help:\t"
                        + ex);
            }
        } else
        {
            JOptionPane.showMessageDialog(this,
                    "Sorry, this feature is not yet implemented.");
        }
    }

    /**
     * When the alert leve changes, this method is responsible for changing the
     * alert level icon (which should alert the user watching the program).
     */
    public void alertLevelChanged(int alertLevel)
    {
        changeAlertIconOnMainFrame(alertLevel);
    }

    /**
     * This method determines which icon should be displayed based on the alert
     * Level.
     */
    private void changeAlertIconOnMainFrame(int alertLevel)
    {
        if (alertLevel == AlertLevel.LOW_ALERT_LEVEL)
        {
            setIconImage(lowAlertIcon.getImage());
        } else if (alertLevel == AlertLevel.MEDIUM_ALERT_LEVEL)
        {
            setIconImage(medAlertIcon.getImage());
        } else if (alertLevel == AlertLevel.HIGH_ALERT_LEVEL)
        {
            setIconImage(highAlertIcon.getImage());
        } else
        {
            setIconImage(noAlertIcon.getImage());
        }
    }

    /** Currently does nothing. */
    public void componentHidden(ComponentEvent e)
    {
    }

    /** Currently does nothing. */
    public void componentMoved(ComponentEvent e)
    {
    }

    /** Currently does nothing. */
    public void componentShown(ComponentEvent e)
    {
    }

    /**
     * This method will resize objects in the form when the window is resized.
     * particularly it resizes the graph. Most of the other objects do this
     * transparently.
     */
    public void componentResized(ComponentEvent e)
    {
        lastHourGraph.setSize(getWidth() - 20, lastHourGraph.getHeight());
        lastHourGraph
                .setSizes(getWidth() - 20, (lastHourGraph.getHeight() - 5));
    }

    public void windowActivated(java.awt.event.WindowEvent e)
    {
    }

    public void windowClosed(java.awt.event.WindowEvent e)
    {
    }

    public void windowClosing(java.awt.event.WindowEvent e)
    {
    }

    public void windowDeactivated(java.awt.event.WindowEvent e)
    {
    }

    public void windowDeiconified(java.awt.event.WindowEvent e)
    {
    }

    public void windowIconified(java.awt.event.WindowEvent e)
    {
    }

    public void windowOpened(java.awt.event.WindowEvent e)
    {
    }
}