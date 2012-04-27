/*
 * TopAttackerVector.java
 *
 * Created on March 22, 2004, 1:10 PM
 */

package com.allthingscompute.sam.database.Objects;

import java.sql.ResultSet;

/**
 * This class is used to maintain a collection of TopAttacker Objects.
 * 
 * @author E. Internicola
 */
public class TopAttackerVector extends DBObjectVector
{
    public TopAttackerVector()
    {
    }

    public void populate(ResultSet rs)
    {
        try
        {
            while (rs.next())
            {
                add(TopAttacker.fromResultSet(rs));
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Exception populating TopAttackerVector:\t" + e);
        }
    }

    /**
     * This method returns a Vector of the top attackers for the last hour (each
     * object is a TopAttacker Object).
     */
    public static TopAttackerVector getTopAttackers()
    {
        return getTopAttackerVector(getDBI().getTopAttackers());
    }

    /**
     * This method is responsible for creating a TopAttackerVector, fetching the
     * ResultSet for the corresponding sql statement, and populating the
     * TopAttackVector with the results.
     */
    private static TopAttackerVector getTopAttackerVector(String strSQL)
    {
        TopAttackerVector tav = new TopAttackerVector();
        ResultSet rs = getResultSet(strSQL);
        tav.populate(rs);
        return tav;
    }

    /**
     * This method will do a host lookup for all of the Host objects for each
     * TopAttacker Object.
     */
    public void lookupAllHosts()
    {
        for (int i = 0; i < size(); i++)
        {
            TopAttacker ta = (TopAttacker) elementAt(i);
            ta.getHost().doLookup();
        }
    }

    /**
     * This method will force a host lookup (regardless of the DisableLookup
     * property) for every host in the TopAttackerVector.
     */
    public void forceLookupAllHosts()
    {
        for (int i = 0; i < size(); i++)
        {
            TopAttacker ta = (TopAttacker) elementAt(i);
            ta.getHost().executeLookup();
        }
    }
}