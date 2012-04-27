
package com.allthingscompute.sam.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.allthingscompute.sam.config.PropertiesManager;
import com.allthingscompute.sam.log.Logger;

/**
 * This class is used to do the lookup of the ports pertianing to a particular
 * event. It will likely be extended later to do more than just this (for
 * instance lookup ICMP, or IP data).
 * 
 * @author E. Internicola
 */
public class EventLookup
{
    /** */
    private String cid = null;

    /** */
    private boolean exists = false;

    /** */
    private boolean isUdp = false;

    /** */
    private boolean isTcp = false;

    /** */
    private boolean isIcmp = false;

    /** */
    private boolean isIp = false;

    /** */
    private String sPort = null;

    /** */
    private String dPort = null;

    /**
     * Constructor - this takes an identifier.
     */
    public EventLookup(String CID)
    {
        cid = new String(CID);
        doLookup();
    }

    /** This method returns true if the event was a UDP Event. */
    public boolean isUDP()
    {
        return isUdp;
    }

    /** This method returns true if the event was a TCP Event. */
    public boolean isTCP()
    {
        return isTcp;
    }

    /**
     * This method will return the Source Port that was read from the database
     * (if it does in fact exist).
     */
    public String getSPort()
    {
        return sPort;
    }

    /**
     * This method will return the destination port that was read from the
     * database (if it does in fact exist).
     */
    public String getDPort()
    {
        return dPort;
    }

    /**
     * This method actually does the work of going out to the database and
     * figuring out if the event is a TCP, or UDP event.
     */
    private void doLookup()
    {
        try
        {
            //variables to be used
            Connection con;
            PreparedStatement ps;
            Integer i;
            ResultSet rs;

            //first; establish our connection
            con = DatabaseConnection.getDatabaseConnection().getConnection();

            //now see if there is a TCP record for the particular event (with
            // the supplied CID ).
            ps = con.prepareStatement(PropertiesManager.getDatabaseInterface()
                    .getPortsTCP());
            i = new Integer(cid.trim());
            ps.setInt(1, i.intValue());
            rs = ps.executeQuery();

            //if there is, then don't lookup the UDP info; just store the data.
            if (rs.next())
            {
                sPort = rs.getString("tcp_sport");
                dPort = rs.getString("tcp_dport");
                isTcp = true;
                rs.close();
                return;
            }
            rs.close();

            //since it wasn't a TCP event, lookup the UDP info, it should be
            // one of these.
            ps = con.prepareStatement(PropertiesManager.getDatabaseInterface()
                    .getPortsUDP());
            ps.setInt(1, i.intValue());
            rs = ps.executeQuery();
            if (rs.next())
            {
                sPort = rs.getString("udp_sport");
                dPort = rs.getString("udp_dport");
                isUdp = true;
            }
            rs.close();
        } catch (Exception e)
        {
            Logger.writeToLog(e.getMessage());
            e.printStackTrace();
        }
    }
}