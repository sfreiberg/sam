package com.allthingscompute.sam.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

//import java.util.StringTokenizer;

/**
 * This class is responsible for reading a configuration file. This class has
 * been recently modified (3/31/2003) to read more things from the database
 * configuration files, and parse more things from what already existed. Added
 * methods are:
 * <UL>
 * <LI>{@link #getURL getURL}</LI>
 * <LI>{@link #getHostname getHostName}</LI>
 * <LI>{@link #getDatabaseName getDatabaseName}</LI>
 * <LI>{@link #getPort getPort}</LI>
 * <LI>{@link #getUsername getUsername}</LI>
 * <LI>{@link #getPassword getPassword}</LI>
 * <LI>{@link #getDriver getDriver}</LI>
 * </UL>
 * 
 * @author Sam, Documented by E. Internicola, Code Updated by E. Internicola
 * @version 0.2
 */
public class DatabaseConfigFile
{
    public static final String CLASS_POSTGRESQL = "org.postgresql.Driver";

    public static final String CLASS_MYSQL = "org.gjt.mm.mysql.Driver";

    private String databaseType = "";

    private File configFile = null;

    private String driver = "";

    private String uid = "";

    private String url = "";

    private String hostname = "";

    private String databaseName = "";

    private String port = "";

    private String username = "";

    private String password = "";

    /** Public method to retreive the Driver. */
    public String getDriver()
    {
        return driver;
    }

    /** Public method to retreive the JDBC url. */
    public String getURL()
    {
        return url;
    }

    /** Public method to retreive the Host Name. */
    public String getHostName()
    {
        return hostname;
    }

    /** Public method to retreive the port */
    public String getPort()
    {
        return port;
    }

    /** Public method to retreive the username */
    public String getUsername()
    {
        return username;
    }

    /** Public method to retreive the password */
    public String getPassword()
    {
        return password;
    }

    /** Get the UID property. */
    public String getUID()
    {
        return uid;
    }

    /** Get the Database Type property. */
    public String getDatabaseType()
    {
        return databaseType;
    }

    /** Get the Database Name property. */
    public String getDatabaseName()
    {
        return databaseName;
    }

    /** Set the UID property. */
    public void setUID(String uid)
    {
        this.uid = uid;
    }

    /** Set the Driver Property. */
    public void setDriver(String driver)
    {
        this.driver = driver;
    }

    /** Set the URL Property. */
    public void setURL(String url)
    {
        this.url = url;
    }

    private void setURL()
    {
        setURL("jdbc:" + getDatabaseType().toLowerCase() + "://"
                + getHostName() + ":" + getPort() + "/" + getDatabaseName());
    }

    public void setHostName(String hostname)
    {
        this.hostname = hostname;
        setURL();
    }

    public void setDatabaseName(String databaseName)
    {
        this.databaseName = databaseName;
        setURL();
    }

    /**
     * This method sets the database type property.
     */
    public void setDatabaseType(String databaseType)
    {
        if (databaseType.toLowerCase().equals("postgresql"))
        {
            this.driver = CLASS_POSTGRESQL;
        } else if (databaseType.toLowerCase().equals("mysql"))
        {
            this.driver = CLASS_MYSQL;
        }
        this.databaseType = databaseType;
        setURL();
    }

    /** Set the port property. This method also resets the URL property. */
    public void setPort(String port)
    {
        this.port = port;
        setURL();
    }

    /** Set the username property. */
    public void setUsername(String username)
    {
        this.username = username;
    }

    /** Set the Password property. */
    public void setPassword(String password)
    {
        this.password = password;
    }

    /**
     * Private method; used to parse the URL to retreive the hostname, port, and
     * database name
     */
    private void parseURL()
    {
        int start = -1;
        int end = -1;
        int i;
        for (i = 0; i < url.length(); i++)
        {
            if (url.charAt(i) == '/')
            {
                start = i + 1;
            } else if (start > -1 && url.charAt(i) == ':')
            {
                end = i;
                break;
            }
        }
        if (end < 0)
            return;

        hostname = url.substring(start, end);
        start = end + 1;
        for (i = start; i < url.length(); i++)
        {
            if (url.charAt(i) == '/')
            {
                end = i;
                break;
            }
        }
        port = url.substring(start, end);
        start = end + 1;
        databaseName = url.substring(start);
    }

    /**
     * Constructor that takes a File object which is the specific database
     * config file.
     */
    public DatabaseConfigFile(File configFile)
    {
        try
        {
            this.configFile = configFile;
            BufferedReader reader = new BufferedReader(new FileReader(
                    configFile));
            while (reader.ready())
            {
                String s = reader.readLine();
                String[] parts = null;

                //grab the database type
                if (s.startsWith("database.type="))
                {
                    try
                    {
                        parts = s.split("=");
                        databaseType = parts[1];
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                        System.out
                                .println("DB Config File Exception (invalid databse type):\t"
                                        + e);
                    }
                }
                //grab the classname
                else if (s.startsWith("driver.classname"))
                {
                    try
                    {
                        parts = s.split("=");
                        driver = parts[1];
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                        System.out
                                .println("DB Config File Exception ( invalid driver classname - "
                                        + configFile.getPath() + "):\t" + e);
                    }
                }
                //grab the url
                else if (s.startsWith("database.url"))
                {
                    try
                    {
                        parts = s.split("=");
                        url = parts[1];
                        parseURL();
                    } catch (Exception e)
                    {
                        url = "";
                    }
                }
                //grab the username
                else if (s.startsWith("database.username"))
                {
                    try
                    {
                        parts = s.split("=");
                        username = parts[1];
                    } catch (Exception e)
                    {
                        username = "";
                    }
                }
                //grab the password
                else if (s.startsWith("database.password"))
                {
                    try
                    {
                        parts = s.split("=");
                        password = parts[1];
                    } catch (Exception e)
                    {
                        password = "";
                    }
                } else if (s.startsWith("database.uid"))
                {
                    try
                    {
                        parts = s.split("=");
                        uid = parts[1];
                    } catch (Exception e)
                    {
                        uid = "Unknown UID";
                    }
                }
            }
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        this.configFile = configFile;
    }

    /** Write the database configuration to the file. */
    public void writeFile()
    {
        String data = "database.uid=" + uid + "\ndatabase.type=" + databaseType
                + "\ndriver.classname=" + driver + "\ndatabase.url=" + url
                + "\ndatabase.username=" + username + "\ndatabase.password="
                + password;
        try
        {
            FileOutputStream fos = new FileOutputStream(configFile, false);
            fos.write(data.getBytes());
            fos.close();
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Exception writing database config file:\t" + e);
        }
    }

    /**
     * Public method to retreive the configuration file.
     */
    public File getFile()
    {
        return configFile;
    }

    /**
     * Public toString method. This method is just the {@link #getName getName}
     * method.
     */
    public String toString()
    {
        return uid;
    }
}