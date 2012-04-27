/*
 * Sensor.java
 *
 * Created on February 26, 2004, 3:03 PM
 */

package com.allthingscompute.sam.database.Objects;

import java.sql.ResultSet;

import com.allthingscompute.sam.net.Lookup;

/**
 * This class is used to maintain information regarding a Sensor Record.
 * 
 * @author E. Internicola
 */
public class Sensor extends DBObject
{
    private Long sid = null;

    private Lookup host = null;

    private String iface = null;

    private String filter = null;

    private Integer detail = null;

    private Integer encoding = null;

    private Long lastCid = null;

    /** Creates a new instance of Sensor */
    public Sensor()
    {
    }

    public Sensor(Sensor s)
    {
        copy(s);
    }

    public Sensor(Long sid)
    {
        copy(getSensor(sid));
    }

    public Sensor(String sid)
    {
        copy(getSensor(sid));
    }

    public Sensor(String sid, String hostName, String iface)
    {
        setSid(sid);
        setHostName(hostName);
        setInterface(iface);
    }

    public Sensor(Long sid, String hostName, String iface)
    {
        setSid(sid);
        setHostName(hostName);
        setInterface(iface);
    }

    public Sensor(Long sid, String hostName, String iface, String filter,
            Integer detail, Integer encoding, Long lasCid)
    {
        setSid(sid);
        setHostName(hostName);
        setInterface(iface);
        setFilter(filter);
        setDetail(detail);
        setEncoding(encoding);
        setLastCid(lastCid);
    }

    public Sensor(String sid, String hostName, String iface, String filter,
            String detail, String encoding, String lasCid)
    {
        setSid(sid);
        setHostName(hostName);
        setInterface(iface);
        setFilter(filter);
        setDetail(detail);
        setEncoding(encoding);
        setLastCid(lastCid);
    }

    public void setLastCid(String lastCid)
    {
        try
        {
            setLastCid(new Long(lastCid));
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Exception setting Last Sid:\t" + e);
        }
    }

    public void setLastCid(Long lastCid)
    {
        this.lastCid = lastCid;
    }

    public void setEncoding(String encoding)
    {
        try
        {
            setEncoding(new Integer(encoding));
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Exception setting encoding:\t" + e);
        }
    }

    public void setEncoding(Integer encoding)
    {
        this.encoding = encoding;
    }

    public void setDetail(String detail)
    {
        try
        {
            setDetail(new Integer(detail));
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Exception setting detail:\t" + e);
        }
    }

    public void setDetail(Integer detail)
    {
        this.detail = detail;
    }

    public void setFilter(String filter)
    {
        this.filter = filter;
    }

    public void setInterface(String iface)
    {
        this.iface = iface;
    }

    public void setHostName(String host)
    {
        this.host = new Lookup(host);
    }

    public void setSid(Long sid)
    {
        this.sid = sid;
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

    public Long getSid()
    {
        return sid;
    }

    public Lookup getHost()
    {
        return host;
    }

    public String getInterface()
    {
        return iface;
    }

    public String getFilter()
    {
        return filter;
    }

    public Integer getDetail()
    {
        return detail;
    }

    public Integer getEncoding()
    {
        return encoding;
    }

    public Long getLastCid()
    {
        return lastCid;
    }

    private void copy(Sensor s)
    {
        this.detail = s.detail;
        this.encoding = s.encoding;
        this.lastCid = s.lastCid;
        this.sid = s.sid;
        this.filter = s.filter;
        this.host = s.host;
        this.iface = s.iface;
    }

    public static Sensor getSensor(String sid)
    {
        try
        {
            return getSensor(new Long(sid));
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Exception retreiving sensor:\t" + e);
        }
        return null;
    }

    public static Sensor getSensor(Long sid)
    {
        ResultSet rs = getResultSet("SELECT * FROM Sensor WHERE sid=" + sid);
        try
        {
            if (rs.next())
            {
                return (Sensor) fromResultSet(rs);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Exception retreiving Sensor:\t" + e);
        }
        return null;
    }

    public static DBObject fromResultSet(ResultSet rs)
    {
        Sensor s = null;
        try
        {
            s = new Sensor(rs.getString("sid"), rs.getString("hostName"), rs
                    .getString("interface"), rs.getString("filter"), rs
                    .getString("detail"), rs.getString("encoding"), rs
                    .getString("last_cid"));
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Exceptiong creating Sensor from ResultSet:\t"
                    + e);
        }
        return s;
    }

    public void delete()
    {
        String strSQL = "DELETE FROM Sensor WHERE sid=" + sid;
        DBObject.executeSQL(strSQL);
    }
}