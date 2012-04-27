package com.allthingscompute.sam.database;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * This class is responsible for handling a connection to a snort database.
 * 
 * @author sfreiberg, Documented by E. Internicola Date Jul 24, 2002
 */
public class DatabaseConnection
{
    /**
     * Static instance of a DatabaseConnection object (self-referencing I
     * presume)
     */
    private static DatabaseConnection databaseConnection = null;

    /** the Username to use when connecting. */
    private String username = null;

    /** the password to use when connecting. */
    private String password = null;

    /** the JDBC URL to use to connect to the database. */
    private String jdbcUrl = null;

    private String driver = null;

    /** the properties of the database connection. */
    private Properties properties = null;

    /** "Auto-Re-Login" property (true by default). */
    private boolean autoReLogin = true;

    /** Boolean flag, logged into the database or not? */
    private boolean loggedIn = false;

    /** Connection Object (the actual database connection). */
    private Connection con;

    public DatabaseConnection(String jdbcUrl, String username, String password,
            String driver) throws Exception
    {
        //this.jdbcUrl = jdbcUrl + "/";
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
        this.driver = driver;
        Class.forName(driver).newInstance();
        login();
    }

    /**
     * Constructor that takes host, table, username, password, and configuration
     * file as parameters. Currently it overwrites data in the file with
     * parameters that are passed in.
     * 
     * @version 0.1
     */
    public DatabaseConnection(DatabaseConfigFile confFile) throws Exception
    {
        String classname;
        properties = new Properties();
        properties.load(new FileInputStream(confFile.getFile()));
        classname = properties.getProperty("driver.classname");
        this.username = properties.getProperty("database.username");
        this.password = properties.getProperty("database.password");
        jdbcUrl = properties.getProperty("database.url");
        Class.forName(classname).newInstance();
        login();
    }

    /**
     * This executes the attempt to lonin to the database depicted by the JDBC
     * URL, username, and password. Exceptions may be thrown if there is a
     * problem connecting.
     */
    public void login() throws Exception
    {
        con = DriverManager.getConnection(jdbcUrl, username, password);
        loggedIn = true;
    }

    public boolean isConnected()
    {
        return loggedIn;
    }

    /**
     * Retreive the connection object.
     */
    public Connection getConnection()
    {
        return con;
    }

    /**
     * Retreive the Properties object.
     */
    public Properties getProperties()
    {
        return properties;
    }

    /**
     * Retreive the DatabaseConnection Object.
     */
    public static DatabaseConnection getDatabaseConnection()
    {
        return databaseConnection;
    }

    /**
     * set the DatabseConnection Object.
     */
    public static void setDatabaseConnection(DatabaseConnection con)
    {
        databaseConnection = con;
    }
}