/*
 * AttackVector.java
 *
 * Created on March 2, 2004, 3:32 PM
 */

package com.allthingscompute.sam.database.Objects;

import java.sql.ResultSet;
import java.util.Collections;

/**
 * This class is used to maintain a collection of Attack Objects (records).
 * 
 * @author E. Internicola
 */
public class AttackVector extends DBObjectVector
{
    /**
     * This method returns an AttackVector of all of the attacks that occured in
     * the last hour.
     */
    public static AttackVector getAttacksLastHour()
    {
        return getAttackVector(getDBI().getAttacksLastHour());
    }

    /**
     * Returns an AttackVector of all of the related Attacks (by attack type).
     */
    public static AttackVector getRelatedAttacks(String attackType, long hours)
    {
        return getAttackVector(getDBI().getRelatedAttacks(attackType, hours));
    }

    /**
     * Retreive an AttackVector of attacks that are related by their source ip.
     */
    public static AttackVector getRelatedAttacksBySource(long srcIP, long hours)
    {
        return getAttackVector(getDBI().getRelatedSources(srcIP, hours));
    }

    /**
     * Retreive an AttackVector of attacks that are related by their destination
     * ip.
     */
    public static AttackVector getRelatedAttacksByDestination(long destIP,
            long hours)
    {
        return getAttackVector(getDBI().getRelatedDestinations(destIP, hours));
    }

    private static AttackVector getAttackVector(String strSQL)
    {
        AttackVector av = new AttackVector();
        ResultSet rs = getResultSet(strSQL);
        av.populate(rs);
        return av;
    }

    /**
     * This method will populate the vector with the corresponding information
     * from the referenced ResultSet.
     */
    public void populate(ResultSet rs)
    {
        try
        {
            this.clear();
            while (rs.next())
            {
                add(Attack.fromResultSet(rs));
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Exception populating AttackVector:\t" + e);
        }
    }

    /**
     * This method *asks* to do a lookup for all hosts, but keeps in mind the
     * settings of the "DisableLookup".
     */
    public void lookupAllHosts()
    {
        for (int i = 0; i < size(); i++)
        {
            Attack a = (Attack) elementAt(i);
            a.getSource().doLookup();
            a.getDestination().doLookup();
        }
    }

    /**
     *  
     */
    public void forceLookupAllHosts()
    {
        for (int i = 0; i < size(); i++)
        {
            Attack a = (Attack) elementAt(i);
            a.getSource().executeLookup();
            a.getDestination().executeLookup();
        }
    }

    /**
     * Sort this vector using the referenced AttackComparator.
     */
    public void sort(AttackComparator ac)
    {
        Collections.sort(this, ac);
    }
}