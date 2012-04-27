/*
 * DBObjectVector.java
 *
 * Created on February 9, 2004, 2:29 PM
 */

package com.allthingscompute.sam.database.Objects;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

import com.allthingscompute.sam.config.PropertiesManager;
import com.allthingscompute.sam.database.DatabaseConnection;
import com.allthingscompute.sam.database.DatabaseInterface;

/**
 * This class is the base class for all of the Database Object Vector classes.
 * They are responsible for hold collections of various abstractions of database
 * relations; whether they be physical relations, or abstracted relations
 * (views).
 * 
 * @see com.allthingscompute.sam.database.Objects.AttackVector
 * @author E. Internicola
 */
public class DBObjectVector extends Vector
{

    protected static Connection objConn = null;

    /**
     * This method is responsible for taking a result set and populating the
     * vector with the given type of DBObject instance for the given ResultSet.
     */
    public void populate(ResultSet rs)
    {
    }

    /**
     * This will retreive the appropriate instance of the DBI object.
     * 
     * @see com.allthingscompute.sam.database.DatabaseInterface
     */
    protected static DatabaseInterface getDBI()
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
     * Retreives a connection.
     */
    protected static final Connection getConnection()
    {
        if (objConn == null)
        {
            objConn = DatabaseConnection.getDatabaseConnection()
                    .getConnection();
        }
        return objConn;
    }

    /**
     * Retreives a ResultSet object from the database.
     */
    protected static final ResultSet getResultSet(String strSQL)
    {
        ResultSet rs = null;
        try
        {
            Statement stmt = null;
            Connection c = getConnection();
            stmt = c.createStatement();
            rs = stmt.executeQuery(strSQL);
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Exception retreiving Result Set:\t" + e);
        }
        return rs;
    }

    /**
     * Executes a SQL statement.
     */
    protected static final void executeSQL(String strSQL)
    {
        try
        {
            Statement stmt = null;
            Connection c = getConnection();
            stmt = c.createStatement();
            stmt.execute(strSQL);
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Exception retreiving Result Set:\t" + e);
        }
    }

}