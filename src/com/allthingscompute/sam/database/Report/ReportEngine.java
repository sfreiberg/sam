/*
 * ReportEngine.java
 *
 * Created on February 5, 2004, 3:31 PM
 */

package com.allthingscompute.sam.database.Report;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.allthingscompute.sam.config.PropertiesManager;
import com.allthingscompute.sam.database.DatabaseConnection;
import com.allthingscompute.sam.database.DatabaseInterface;
import com.allthingscompute.sam.database.Objects.AttackVector;

/**
 * This class is used to retreive varous reports.
 * 
 * @author E. Internicola
 */
public class ReportEngine
{
    /**
     * Retreive the instance of a Connection object. This object should be
     * opened (as long as the user has selected a database to connect to).
     */
    private static Connection getConnection()
    {
        return DatabaseConnection.getDatabaseConnection().getConnection();
    }

    /**
     * Retreive a ResultSet of attacks that are related by attack type.
     */
    public static AttackVector getRelatedAttacks(String attackType, long hours)
    {
        return AttackVector.getRelatedAttacks(attackType, hours);
    }

    /**
     * Retreive a ResultSet of attacks that are related by their source ip.
     */
    public static AttackVector getRelatedAttacksBySource(long srcIP, long hours)
    {
        return AttackVector.getRelatedAttacksBySource(srcIP, hours);
    }

    /**
     * Retreive a ResultSet of attacks that are related by their destination ip.
     */
    public static AttackVector getRelatedAttacksByDestination(long destIP,
            long hours)
    {
        return AttackVector.getRelatedAttacksByDestination(destIP, hours);
    }

    /**
     * Retreive the data for a paticular attack (by its cid).
     */
    public static ResultSet getData(long sid, long cid)
    {
        return getResultSet(getDBI().getData(sid, cid));
    }

    /**
     * Retreive the Signature for a particular attack (by its cid).
     */
    public static ResultSet getSignature(long sid, long cid)
    {
        return getResultSet(getDBI().getSignature(sid, cid));
    }

    /**
     * This will retreive the appropriate instance of the DBI object.
     * 
     * @see com.allthingscompute.sam.database.DatabaseInterface
     */
    private static DatabaseInterface getDBI()
    {
        try
        {
            //PropertiesManager pm = PropertiesManager.getPropertiesManager();
            return PropertiesManager.getDatabaseInterface();
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Exception getting database interface:\t" + e);
        }
        return null;
    }

    /**
     * This method will retreive a ResultSet based on whatever SQL (SELECT)
     * statement you provide (as strSQL).
     * 
     * @see #getConnection
     */
    private static ResultSet getResultSet(String strSQL)
    {
        try
        {
            ResultSet rs = null;
            Statement stmt = null;
            Connection c = getConnection();
            stmt = c.createStatement();
            rs = stmt.executeQuery(strSQL);
            return rs;
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Exception getting related attacks:\t" + e);
        }
        return null;
    }
}