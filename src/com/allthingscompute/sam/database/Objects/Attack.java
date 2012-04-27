/*
 * Attack.java
 *
 * Created on February 26, 2004, 3:00 PM
 */

package com.allthingscompute.sam.database.Objects;

import java.sql.ResultSet;

import com.allthingscompute.sam.net.Lookup;

/**
 * This class is used to maintain information regarding a single attack.
 * 
 * @author E. Internicola
 */
public class Attack extends DBObject
{
    private Long sid = null;

    private Long cid = null;

    private Lookup source = null;

    private Lookup destination = null;

    private Sensor sensor = null;

    private Signature signature = null;

    private DateTime timestamp = null;

    public Long getSid()
    {
        return sid;
    }

    public Long getCid()
    {
        return cid;
    }

    public Lookup getSource()
    {
        return source;
    }

    public Lookup getDestination()
    {
        return destination;
    }

    public Sensor getSensor()
    {
        return sensor;
    }

    public Signature getSignature()
    {
        return signature;
    }

    public DateTime getTimestamp()
    {
        return timestamp;
    }

    public Attack()
    {
    }

    public Attack(Long sid, Long cid)
    {
        setSid(sid);
        setCid(cid);
    }

    public Attack(String sid, String cid)
    {
        setSid(sid);
        setCid(cid);
    }

    public Attack(Long sid, Long cid, Long source, Long destination,
            Long sigID, String timestamp)
    {
        setSid(sid);
        setCid(cid);
        setSource(source);
        setDestination(destination);
        setSignature(sigID);
        setTimestamp(timestamp);
    }

    public Attack(String sid, String cid, String source, String destination,
            String sigID, String timestamp)
    {
        setSid(sid);
        setCid(cid);
        setSource(source);
        setDestination(destination);
        setSignature(sigID);
        setTimestamp(timestamp);
    }

    public void setTimestamp(String timestamp)
    {
        this.timestamp = new DateTime(timestamp);
    }

    public void setTimestamp(DateTime timestamp)
    {
        this.timestamp = new DateTime(timestamp);
    }

    public void setSignature(Long sigID)
    {
        this.signature = new Signature(sigID);
    }

    public void setSignature(String sigID)
    {
        this.signature = new Signature(sigID);
    }

    public void setDestination(Long destination)
    {
        this.destination = new Lookup(destination.longValue());
    }

    public void setDestination(String destination)
    {
        this.destination = new Lookup(destination);
    }

    public void setSource(Long source)
    {
        this.source = new Lookup(source.longValue());
    }

    public void setSource(String source)
    {
        this.source = new Lookup(source);
    }

    public void setSid(Long sid)
    {
        this.sid = sid;
    }

    public void setCid(Long cid)
    {
        this.cid = cid;
    }

    public void setSid(String sid)
    {
        try
        {
            setSid(new Long(sid));
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Exception setting sid:\t" + e);
        }
    }

    public void setSensor(String sid)
    {
        this.sensor = new Sensor(sid);
    }

    public void setSensor(Long sid)
    {
        this.sensor = new Sensor(sid);
    }

    public void setSensor(Long sid, String hostName, String iface)
    {
        this.sensor = new Sensor(sid, hostName, iface);
    }

    public void setSensor(String sid, String hostName, String iface)
    {
        this.sensor = new Sensor(sid, hostName, iface);
    }

    public void setSensor(Sensor s)
    {
        this.sensor = new Sensor(s);
    }

    public void setCid(String cid)
    {
        try
        {
            setCid(new Long(cid));
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Exception setting cid:\t" + e);
        }
    }

    public void setSignature(Signature s)
    {
        this.signature = new Signature(s);
    }

    public Attack getAttack(Long sid, Long cid)
    {
        try
        {
            ResultSet rs = Attack
                    .getResultSet("SELECT SEN.*, SIG.*, E.*, IP.* FROM Sensor SEN, "
                            + "Signature SIG, Event E, IPHdr IP\nWHERE SEN.sid=E.sid\nANDSIG.sig_id=E.signature\n"
                            + "AND IP.sid=E.sid\nAND IP.cid=E.cid\nAND E.sid="
                            + sid + "\nAND E.cid=" + cid);
            if (rs.next())
            {
                return (Attack) fromResultSet(rs);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Exception getting Attack:\t" + e);
        }
        return null;
    }

    public static DBObject fromResultSet(ResultSet rs)
    {
        Attack a = null;
        Sensor se = null;
        Signature si = null;
        try
        {
            a = new Attack(rs.getString("sid"), rs.getString("cid"), rs
                    .getString("ip_src"), rs.getString("ip_dst"), rs
                    .getString("signature"), rs.getString("timestamp"));
            se = new Sensor(rs.getString("sid"), rs.getString("hostname"), rs
                    .getString("interface"));
            a.setSensor(se);
            si = new Signature(rs.getString("sig_id"),
                    rs.getString("sig_name"), rs.getString("sig_class_id"), rs
                            .getString("sig_priority"),
                    rs.getString("sig_rev"), rs.getString("sig_sid"));
            a.setSignature(si);
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Exception creating Attack from ResultSet:\t"
                    + e);
        }
        return a;
    }
}