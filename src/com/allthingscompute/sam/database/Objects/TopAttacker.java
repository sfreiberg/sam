/*
 * TopAttacker.java
 *
 * Created on March 22, 2004, 12:36 PM
 */

package com.allthingscompute.sam.database.Objects;

import java.sql.ResultSet;

import com.allthingscompute.sam.net.Lookup;

/**
 * 
 * @author E. Internicola
 */
public class TopAttacker extends DBObject
{
    private Lookup host = null;

    private Long count = null;

    public TopAttacker()
    {
    }

    public TopAttacker(String host, String count)
    {
        setHost(host);
        setCount(count);
    }

    /** Set the Host. */
    public void setHost(String host)
    {
        this.host = new Lookup(host);
    }

    /** Set the Count. */
    public void setCount(Long count)
    {
        this.count = count;
    }

    /** Set the Count. */
    public void setCount(String count)
    {
        try
        {
            setCount(new Long(count));
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Exception setting Count:\t" + e);
        }
    }

    /** Returns the Host (Lookup) object. */
    public Lookup getHost()
    {
        return host;
    }

    /** Returns the Count. */
    public Long getCount()
    {
        return count;
    }

    public static DBObject fromResultSet(ResultSet rs)
    {
        try
        {
            return new TopAttacker(rs.getString("ip_addr"), rs
                    .getString("num_events"));
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out
                    .println("Exception creating TopAttacker from result set:\t"
                            + e);
        }
        return null;
    }
}