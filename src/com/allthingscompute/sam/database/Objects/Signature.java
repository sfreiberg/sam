/*
 * Signature.java
 *
 * Created on February 26, 2004, 4:02 PM
 */

package com.allthingscompute.sam.database.Objects;

import java.sql.ResultSet;

/**
 * This class is used to maintain information about a Signature Record.
 * 
 * @author E. Internicola
 */
public class Signature extends DBObject
{
    private Long sigID = null;

    private String sigName = null;

    private Long sigClassID = null;

    private Long sigPriority = null;

    private Long sigRev = null;

    private Long sigSid = null;

    public Long getSigID()
    {
        return sigID;
    }

    public String getSigName()
    {
        return sigName;
    }

    public Long getSigClassID()
    {
        return sigClassID;
    }

    public Long getSigPriority()
    {
        return sigPriority;
    }

    public Long getSigRev()
    {
        return sigRev;
    }

    public Long getSigSid()
    {
        return sigSid;
    }

    /** Creates a new instance of Signature */
    public Signature()
    {
    }

    public Signature(Signature s)
    {
        copy(s);
    }

    public Signature(String sigID, String sigName, String sigClassID,
            String sigPriority, String sigRev, String sigSid)
    {
        setSigID(sigID);
        setSigName(sigName);
        setSigClassID(sigClassID);
        setSigPriority(sigPriority);
        setSigRev(sigRev);
        setSigSid(sigSid);
    }

    public Signature(Long sigID, String sigName, Long sigClassID,
            Long sigPriority, Long sigRev, Long sigSid)
    {
        setSigID(sigID);
        setSigName(sigName);
        setSigClassID(sigClassID);
        setSigPriority(sigPriority);
        setSigRev(sigRev);
        setSigSid(sigSid);
    }

    public Signature(Long sigID, String sigName)
    {
        setSigID(sigID);
        setSigName(sigName);
    }

    public Signature(String sigID, String sigName)
    {
        setSigID(sigID);
        setSigName(sigName);
    }

    public Signature(Long sigID)
    {
        setSigID(sigID);
    }

    public Signature(String sigID)
    {
        setSigID(sigID);
    }

    public void setSigSid(String sigSid)
    {
        try
        {
            setSigSid(new Long(sigSid));
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Exception setting Sig Sid:\t" + e);
        }
    }

    public void setSigSid(Long sigSid)
    {
        this.sigSid = sigSid;
    }

    public void setSigRev(String sigRev)
    {
        try
        {
            setSigRev(new Long(sigRev));
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Exception setting Sig Rev:\t" + e);
        }
    }

    public void setSigRev(Long sigRev)
    {
        this.sigRev = sigRev;
    }

    public void setSigPriority(String sigPriority)
    {
        try
        {
            setSigPriority(new Long(sigPriority));
        } catch (Exception e)
        {
            System.out.println("Exception setting Signature Priority:\t" + e);
        }
    }

    public void setSigPriority(Long sigPriority)
    {
        this.sigPriority = sigPriority;
    }

    public void setSigClassID(String sigClassID)
    {
        try
        {
            if (sigClassID != null)
            {
                setSigClassID(new Long(sigClassID));
            } else
            {
                sigClassID = null;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Exception setting Sig Class ID:\t" + e);
        }
    }

    public void setSigClassID(Long sigClassID)
    {
        this.sigClassID = sigClassID;
    }

    public void setSigName(String sigName)
    {
        this.sigName = sigName;
    }

    public void setSigID(String sigID)
    {
        try
        {
            setSigID(new Long(sigID));
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Exception setting Sig ID:\t" + e);
        }
    }

    public void setSigID(Long sigID)
    {
        this.sigID = sigID;
    }

    public static Signature getSignature(Long sigID)
    {
        Signature s = null;
        try
        {
            ResultSet rs = getResultSet("SELECT * FROM Signature WHERE sig_id="
                    + sigID);
            if (rs.next())
            {
                s = null;
            }
        } catch (Exception e)
        {

        }
        return s;
    }

    public static DBObject fromResultSet(ResultSet rs)
    {
        Signature s = null;
        try
        {
            s = new Signature(rs.getString("sig_id"), rs.getString("sig_name"),
                    rs.getString("sig_class_id"), rs.getString("sig_priority"),
                    rs.getString("sig_rev"), rs.getString("sig_sid"));
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out
                    .println("Exception retreiving Signature from ResultSet:\t"
                            + e);
        }
        return s;
    }

    private void copy(Signature s)
    {
        this.sigClassID = s.sigClassID;
        this.sigID = s.sigID;
        this.sigPriority = s.sigPriority;
        this.sigRev = s.sigRev;
        this.sigSid = s.sigSid;
        this.sigName = s.sigName;
    }
}