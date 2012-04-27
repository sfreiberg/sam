/*
 * DBObject.java
 *
 * Created on February 9, 2004, 1:34 PM
 */

package com.allthingscompute.sam.database.Objects;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.allthingscompute.sam.database.DatabaseConnection;

/**
 * 
 * @author E. Internicola
 */
public class DBObject
{
    protected static Connection objConn = null;

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

    /** Creates a new instance of DBObject */
    public DBObject()
    {
    }

    //-------------------------------------------------------------------
    //  Methods to be over-ridden
    //-------------------------------------------------------------------

    public void delete()
    {
        return;
    }

    /**
     * This method is to be overridden by all subclasses of this object. This
     * method should read a result set and create the correct type of
     * sub-component.
     */
    public static DBObject fromResultSet(ResultSet rs)
    {
        return null;
    }
}