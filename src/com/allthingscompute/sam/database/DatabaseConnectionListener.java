package com.allthingscompute.sam.database;

/**
 * This interface is used to
 * 
 * @author sfreiberg, Documented by E. Internicola <BR>
 *         Date Jul 25, 2002
 */
public interface DatabaseConnectionListener
{
    /**
     * Event - when a connection to the database is established.
     */
    public void databaseConnectionEstablished(DatabaseConnection con);

    /**
     * Event - when a connection to the database is closed.
     */
    public void databaseConnectionClosed(DatabaseConnection con);
}