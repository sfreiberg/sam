package com.allthingscompute.sam.monitor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JProgressBar;

import com.allthingscompute.sam.Common;
import com.allthingscompute.sam.alert.AlertLevel;
import com.allthingscompute.sam.alert.AlertPriorityTotals;
import com.allthingscompute.sam.alert.AlertTotalsPerProtocol;
import com.allthingscompute.sam.config.PropertiesManager;
import com.allthingscompute.sam.database.DatabaseConnection;
import com.allthingscompute.sam.database.DatabaseInterface;
import com.allthingscompute.sam.database.MySQLInterface;
import com.allthingscompute.sam.database.PostgreSQLInterface;
import com.allthingscompute.sam.database.Objects.Attack;
import com.allthingscompute.sam.database.Objects.AttackVector;
import com.allthingscompute.sam.database.Objects.TopAttacker;
import com.allthingscompute.sam.database.Objects.TopAttackerVector;
import com.allthingscompute.sam.log.Logger;
import com.allthingscompute.sam.net.Lookup;

/**
 * This class is used to monitor Attacks via the snort database. It works by
 * polling the database every N minutes, where N is defaulted to 5 unless you
 * provide N with the constructor, in that case it will be whatever you specify.
 * 
 * @author sfreiberg, Documented by E. Internicola <BR>
 *         Date Jun 24, 2002 <BR>
 *         Updated by E. Internicola (05/05/2003)
 */
public class TimedMonitor extends Monitor implements PropertyChangeListener
{
    // how long between refresh intervals
    private int minutesUntilRefresh;

    // Alert levels greater than this in 5 minutes
    // will set the attack level to high
    private int highAttackLevel;

    // Alert levels greater than this in 5 minutes
    // will set the attack level to medium
    private int mediumAttackLevel;

    private AttackVector av = null;

    private TopAttackerVector tav = null;
    
    private AlertPriorityTotals apt = null;
    
    private AlertTotalsPerProtocol alertTotalsPerProtocol = null;

    /**
     * Variables for holding attack info
     */
    private String attacksLast5Min = "No data", attacksLast15Min = "No data",
            attacksLast30Min = "No data", attacksLast60Min = "No data",
            totalAttacks = "No data", lastUpdated = "No data";

    private Vector topAttackers, topAttacks, topAttacksLastHour;

    private JProgressBar progressBar;

    private String numOfAttacksSql = null;

    private String numOfAttacksForeverSql = null;

    private String curDatabaseTimeSql = null;

    private String topAttackersSql = null;

    private String topAttacksSql = null;
    
    private String alertsBySeveritySql = null;
    
    private String alertsByProtocolSql = null;

    private int currentAlertLevel = AlertLevel.NO_ALERT_LEVEL;

    private Lookup lookup;

    /**
     * Default constructor, sets the number of minutes until a refresh to 5, and
     * the alert level to
     * {@link com.allthingscompute.sam.alert.AlertLevel#NO_ALERT_LEVEL NO_ALERT_LEVEL}.
     * 
     * @see com.allthingscompute.sam.alert.AlertLevel
     */
    public TimedMonitor() throws Exception
    {
        topAttackers = new Vector();
        topAttacks = new Vector();
        topAttacksLastHour = new Vector();
        apt = new AlertPriorityTotals();
        reloadProperties();
        //System.out.println("minutesUntilRefresh: " + this.minutesUntilRefresh);
        PropertiesManager.getPropertiesManager()
                .addPropertyChangeListener(this);
    }

    /**
     * This method allows you to set the DatabaseInterface object based on
     * whatever database you might be using to manage snort. The idea here is
     * that this interface can be extended to whatever type of database you
     * might be using, all that needs to be done to extend this is to add a new
     * class for a given database type that extends the DatabaseInterface
     * interface.
     */
    public void setDatabaseInterface()
    {
        DatabaseInterface di = PropertiesManager.getDatabaseInterface();
        numOfAttacksSql = di.getNumOfAttacks();
        numOfAttacksForeverSql = di.getNumOfAttacksForever();
        curDatabaseTimeSql = di.getCurDatabaseTime();
        topAttackersSql = di.getTopAttackers();
        topAttacksSql = di.getTopAttacks();
        alertsBySeveritySql = di.getAlertsBySeverity();
        alertsByProtocolSql = di.getAlertsByProtocol();
        //attacksLastHourSql = di.getAttacksLastHour();
    }

    /**
     * Constructor that takes takes 2 parameters.
     */
    public TimedMonitor(int minutesUntilRefresh, int alertLevel)
            throws Exception
    {
        topAttackers = new Vector();
        topAttacks = new Vector();
        topAttacksLastHour = new Vector();
        alertTotalsPerProtocol = new AlertTotalsPerProtocol();
        PropertiesManager pm = PropertiesManager.getPropertiesManager();
        mediumAttackLevel = Integer.parseInt(pm.getValue("alertlevel.medium"));
        highAttackLevel = Integer.parseInt(pm.getValue("alertlevel.high"));
        this.minutesUntilRefresh = minutesUntilRefresh;
        System.out.println("minutesUntilRefresh: " + this.minutesUntilRefresh);
        PropertiesManager.getPropertiesManager()
                .addPropertyChangeListener(this);
    }

    /**
     *  
     */
    private void reloadProperties() throws Exception
    {
        PropertiesManager pm = PropertiesManager.getPropertiesManager();
        mediumAttackLevel = Integer.parseInt(pm.getValue("alertlevel.medium"));
        highAttackLevel = Integer.parseInt(pm.getValue("alertlevel.high"));
        minutesUntilRefresh = Integer
                .parseInt(pm.getValue("mainpanel.refresh"));

        String db = pm.getValue("DatabaseType").toLowerCase();
        if (db.compareTo("postgresql") == 0)
        {
            PropertiesManager.setDatabaseInterface(new PostgreSQLInterface());
        } else if (db.compareTo("mysql") == 0)
        {
            PropertiesManager.setDatabaseInterface(new MySQLInterface());
        }
    }

    /**
     * This method keeps allows you to set the progress bar that this class will
     * keep track of to display the progress.
     */
    public void setProgressBar(JProgressBar progressBar)
    {
        this.progressBar = progressBar;
    }

    /**
     * Method to retreive the progress bar object.
     */
    public JProgressBar getProgressBar()
    {
        return progressBar;
    }

    /**
     * This method will re-run each of the queries and display the results on
     * the panel.
     * 
     * @see com.allthingscompute.sam.monitor.Monitor#update()
     */
    public void update()
    {
        int newAlertLevel = AlertLevel.LOW_ALERT_LEVEL;
        String attacks5Min = "No data", attacks15Min = "No data", attacks30Min = "No data", attacks60Min = "No data", totAttacks = "No data", time = "No data";
        topAttackers.clear();
        topAttacks.clear();
        topAttacksLastHour.clear();
        ResultSet rs = null;

        setDatabaseInterface();

        try
        {
            //First, set the progress bar value to 0%
            progressBar.setValue(0);
            progressBar.setString("0%");

            //now connect to the database
            Connection con = DatabaseConnection.getDatabaseConnection()
                    .getConnection();

            //now set the progress bar value to 10%
            progressBar.setValue(1);
            progressBar.setString("10%");

            //Retreive the total number of attacks in the table.
            PreparedStatement ps = con.prepareStatement(numOfAttacksSql);
            ps.setInt(1, 5);
            rs = ps.executeQuery();
            if (rs.next())
            {
                attacks5Min = rs.getString(1);
                if (rs.getInt(1) >= highAttackLevel)
                {
                    newAlertLevel = AlertLevel.HIGH_ALERT_LEVEL;
                } else if (rs.getInt(1) >= mediumAttackLevel)
                {
                    newAlertLevel = AlertLevel.MEDIUM_ALERT_LEVEL;
                } else
                {
                    newAlertLevel = AlertLevel.LOW_ALERT_LEVEL;
                }
            } else
            {
                newAlertLevel = AlertLevel.LOW_ALERT_LEVEL;
            }
            rs = null;
            ps = null;
            progressBar.setValue(2);
            progressBar.setString("20%");
            ps = con.prepareStatement(numOfAttacksSql);
            ps.setInt(1, 15);
            rs = ps.executeQuery();
            if (rs.next())
            {
                attacks15Min = rs.getString(1);
            }
            rs = null;
            ps = null;
            progressBar.setValue(3);
            progressBar.setString("30%");
            ps = con.prepareStatement(numOfAttacksSql);
            ps.setInt(1, 30);
            rs = ps.executeQuery();
            if (rs.next())
            {
                attacks30Min = rs.getString(1);
            }
            rs = null;
            ps = null;
            progressBar.setValue(4);
            progressBar.setString("40%");
            ps = con.prepareStatement(numOfAttacksSql);
            ps.setInt(1, 60);
            rs = ps.executeQuery();
            if (rs.next())
            {
                attacks60Min = rs.getString(1);
            }
            rs = null;
            ps = null;
            progressBar.setValue(5);
            progressBar.setString("50%");
            ps = con.prepareStatement(numOfAttacksForeverSql);
            rs = ps.executeQuery();
            if (rs.next())
            {
                totAttacks = rs.getString(1);
            }
            rs = null;
            ps = null;
            progressBar.setValue(6);
            progressBar.setString("60%");
            ps = con.prepareStatement(curDatabaseTimeSql);
            rs = ps.executeQuery();
            if (rs.next())
            {
                time = rs.getString(1);
            }
            rs = null;
            ps = null;
            progressBar.setValue(7);
            progressBar.setString("70%");
            //ps = con.prepareStatement( topAttackersSql );
            //rs = ps.executeQuery();
            tav = TopAttackerVector.getTopAttackers();
            tav.lookupAllHosts();
            for (int i = 0; i < tav.size(); i++)
            {
                TopAttacker ta = (TopAttacker) tav.elementAt(i);
                Vector v = new Vector(2);
                v.add(ta.getHost().getCanonical());
                v.add(ta.getCount());
                topAttackers.add(v);
            }
            /**
             * while ( rs.next() ) { Vector v = new Vector( 2 ); lookup = new
             * Lookup( rs.getString(1) ); v.add( lookup.toString() );
             * v.add(rs.getString( 2 ) ); topAttackers.add(v); }
             */
            rs = null;
            ps = null;
            progressBar.setValue(8);
            progressBar.setString("80%");
            ps = con.prepareStatement(topAttacksSql);
            rs = ps.executeQuery();
            while (rs.next())
            {
                Vector v = new Vector(2);
                v.add(rs.getString(1));
                v.add(rs.getString(2));
                topAttacks.add(v);
            }
            rs = null;
            ps = null;
            progressBar.setValue(9);
            progressBar.setString("90%");
            
            ps = con.prepareStatement(alertsBySeveritySql);
            rs = ps.executeQuery();
            apt = new AlertPriorityTotals();
            while(rs.next())
            {
                apt.setTotal(rs.getInt("sig_priority"), rs.getInt("total"));
            }
            ps.close();
            rs.close();
            
            ps = con.prepareStatement(alertsByProtocolSql);
            rs = ps.executeQuery();
            alertTotalsPerProtocol = new AlertTotalsPerProtocol();
            while (rs.next())
            {
                if (alertTotalsPerProtocol == null) System.out.println("atpp is null :(");
                alertTotalsPerProtocol.setTotal(rs.getInt("ip_proto"), rs.getInt("total"));
            }
            rs.close();
            ps.close();
            
            //ps = con.prepareStatement( attacksLastHourSql );
            //rs = ps.executeQuery();
            av = AttackVector.getAttacksLastHour();
            for (int i = 0; i < av.size(); i++)
            {
                Attack a = (Attack) av.elementAt(i);
                Vector v = new Vector(5);
                v.add(a.getSensor().getInterface());
                v.add(a.getSensor().getHost().getIP());

                v.add(a.getSource().toString());
                v.add(a.getDestination().toString());

                v.add(a.getSignature().getSigName());
                v.add(a.getTimestamp());
                topAttacksLastHour.add(v);
            }

            progressBar.setValue(10);
            progressBar.setString("100%");

            progressBar.setValue(0);
            progressBar.setString("");
        } catch (SQLException e)
        {
            Logger.writeToLog(e.getMessage());
            e.printStackTrace();
        }

        setNumOfAttacksLast5Min(attacks5Min);
        setNumOfAttacksLast15Min(attacks15Min);
        setNumOfAttacksLast30Min(attacks30Min);
        setNumOfAttacksLast60Min(attacks60Min);
        setTotalNumOfAttacks(totAttacks);
        setLastUpdatedTime(time);

        // let everybody know that this monitor has changed
        fireMonitorChanged(this);

        if (newAlertLevel != currentAlertLevel)
        {
            currentAlertLevel = newAlertLevel;
            fireAlertChangeListeners(currentAlertLevel);
        }
    }

    /**
     * This method will return a string that tells the number of attacks in the
     * last 5 minutes.
     */
    public String getNumOfAttacksLast5Min()
    {
        return attacksLast5Min;
    }

    /**
     * This method will let you set a string that tells the number of attacks in
     * the last 5 minutes.
     */
    private void setNumOfAttacksLast5Min(String s)
    {
        attacksLast5Min = s;
    }

    /**
     * This method will return a string that tells the number of attacks in the
     * last 15 minutes.
     */
    public String getNumOfAttacksLast15Min()
    {
        return attacksLast15Min;
    }

    /**
     * This method will allow you to set a string pertaining to the number of
     * attacks in the last 15 minutes.
     */
    private void setNumOfAttacksLast15Min(String s)
    {
        attacksLast15Min = s;
    }

    /**
     * This method will return a String that contains the number of attacks in
     * the last 30 minutes. This data is retreived from a Database Query.
     */
    public String getNumOfAttacksLast30Min()
    {
        return attacksLast30Min;
    }

    /**
     * This method will allow you set a string that contains the number of
     * attacks in the last 30 minutes.
     */
    private void setNumOfAttacksLast30Min(String s)
    {
        attacksLast30Min = s;
    }

    /**
     * This method will allow you to get a string that contains the number of
     * attacks in the last 60 minutes. This data is retreived from a database
     * query.
     */
    public String getNumOfAttacksLast60Min()
    {
        return attacksLast60Min;
    }

    /**
     * This method allows you to set a string that contains the number of
     * attacks in the last 60 minutes.
     */
    private void setNumOfAttacksLast60Min(String s)
    {
        attacksLast60Min = s;
    }

    /**
     * This method will allow you to retreive a string that contains the total
     * number of attacks in the entire snort event table.
     */
    public String getTotalNumOfAttacks()
    {
        return totalAttacks;
    }

    /**
     * This method will allow you set a string that contains the total number of
     * attacks in the event table (although this should be a reflection of what
     * is in the event table of the snort database).
     */
    private void setTotalNumOfAttacks(String totalAttacks)
    {
        this.totalAttacks = totalAttacks;
    }

    /**
     * This method will return a string containing the last time at which the
     * data was updated.
     */
    public String getLastUpdatedTime()
    {
        return Common.getSequelTime(lastUpdated);
    }

    /**
     * This method will allow you tset a string containg the last time at which
     * the data was updated.
     */
    private void setLastUpdatedTime(String time)
    {
        lastUpdated = time;
    }

    /**
     * This method will return a vector of the top attackers (from the past 24
     * hours).
     */
    public Vector getTopAttackers()
    {
        return topAttackers;
    }

    /**
     * This method will return a vector of the top attacks (from the past 24
     * hours).
     */
    public Vector getTopAttacks()
    {
        return topAttacks;
    }

    /**
     *  
     */
    public Vector getTopAttacksLastHour()
    {
        return topAttacksLastHour;
    }

    /**
     * This method returns the Top Attacker Vector.
     */
    public TopAttackerVector getTopAttackerVector()
    {
        return tav;
    }
    
    /**
     * @return Returns the alertTotalsPerProtocol.
     */
    public AlertTotalsPerProtocol getAlertTotalsPerProtocol()
    {
        return alertTotalsPerProtocol;
    }
    /**
     * This method returns the Attack Vector.
     */
    public AttackVector getAttackVector()
    {
        return av;
    }

    /**
     * This starts up the thread, so that it will poll every minutesUntilRefresh
     * Minutes.
     */
    public void run()
    {
        int MS = 1000;
        int SEC = 60;
        while (true)
        {
            update();
            try
            {
                sleep(minutesUntilRefresh * MS * SEC);
            } catch (InterruptedException e)
            {
                Logger.writeToLog(e.getMessage());
            }
        }
    }

    /**
     * This method is called in the event that a property change occurs. If the
     * alert level changes
     */
    public void propertyChange(PropertyChangeEvent event)
    {
        try
        {
            reloadProperties();
            //update();
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Exception reloading Properties:\t" + e);
        }
    }
    
    /**
     * @return Returns the apt.
     */
    public AlertPriorityTotals getAlertPriorityTotals()
    {
        return apt;
    }
}