/*
 * ThreadedRelatedAttackReport.java
 *
 * Created on March 2, 2004, 1:55 AM
 */

package com.allthingscompute.sam.gui.Reports;

/**
 * This class is used to create an instance of a RelatedAttackReport object; but
 * inside a thread.
 * 
 * @author E. Internicola
 */
public class ThreadedRelatedAttackReport extends Thread
{
    private Long ipAddr = null;

    private Long hours = null;

    private Integer type = null;

    private String attackType = null;

    private RelatedAttackReport rar = null;

    /**
     *  
     */
    public ThreadedRelatedAttackReport(long ipAddr, long hours, int type)
    {
        this.ipAddr = new Long(ipAddr);
        this.hours = new Long(hours);
        this.type = new Integer(type);
        start();
    }

    /**
     *  
     */
    public ThreadedRelatedAttackReport(String attackType, long hours)
    {
        this.attackType = new String(attackType);
        this.hours = new Long(hours);
        start();
    }

    /**
     * This method will show all of the attacks in the past hours - hours where
     * the attack type is the same as the one provided.
     */
    public static void showRelatedAttacks(String attackType, long hours)
    {
        if (attackType != null)
        {
            new ThreadedRelatedAttackReport(attackType, hours);
        }
    }

    /**
     * This method will show all of the attacks in the past hours - hours where
     * the Destination ip is the same as the given ip. (note - the ipAddr is
     * actually a Long Integer, not Dotted Decimal String).
     */
    public static void showRelatedDest(String ipAddr, long hours)
    {
        try
        {
            if (ipAddr == null)
            {
                return;
            }
            Long lIpAddr = new Long(ipAddr);
            new ThreadedRelatedAttackReport(lIpAddr.longValue(), hours,
                    RelatedAttackReport.RELATED_DEST);
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out
                    .println("Exception showing related Destination Report:\t"
                            + e);
        }
    }

    public static void showRelatedDest(long ipAddr, long hours)
    {
        new ThreadedRelatedAttackReport(ipAddr, hours,
                RelatedAttackReport.RELATED_DEST);
    }

    public static void showRelatedSource(long ipAddr, long hours)
    {
        new ThreadedRelatedAttackReport(ipAddr, hours,
                RelatedAttackReport.RELATED_SOURCE);
    }

    /**
     * This method will show a related Source Attack for the given number of
     * hours.
     */
    public static void showRelatedSource(String ipAddr, long hours)
    {
        try
        {
            if (ipAddr == null)
            {
                return;
            }
            Long lIpAddr = new Long(ipAddr);
            new ThreadedRelatedAttackReport(lIpAddr.longValue(), hours,
                    RelatedAttackReport.RELATED_SOURCE);
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out
                    .println("Exception showing Related Source Report:\t" + e);
        }
    }

    /**
     * The actual "work horse" of the class. This is where the code is actually
     * run (as its own thread).
     */
    public void run()
    {
        if (attackType != null)
        {
            rar = new RelatedAttackReport(attackType, hours.intValue());
        } else
        {
            rar = new RelatedAttackReport(ipAddr.longValue(), hours.intValue(),
                    type.intValue());
        }
    }
}