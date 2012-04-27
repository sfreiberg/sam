package com.allthingscompute.sam.config;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import com.allthingscompute.sam.database.DatabaseInterface;

/**
 * This class is used to create a "global" set of properties related to the SAM
 * application. It also is used to read and write these properties to a file (To
 * be exact, it is the file: conf/sam.properties).
 * 
 * @author sfreiberg, Documented by E. Internicola <BR>
 *         Date Jul 29, 2002
 */
public class PropertiesManager
{
    /** Keep a reference to itself. */
    protected static PropertiesManager propsManager;

    /** */
    protected Properties props;

    /** */
    protected File propsFile;

    /** */
    protected Vector propertyChangeListeners = new Vector();

    /** */
    static protected DatabaseInterface di;

    /**
     * This method allows you to store a static copy of the database interface
     * that is being used.
     */
    public static void setDatabaseInterface(DatabaseInterface DI)
    {
        di = DI;
    }

    public Properties getProperties()
    {
        return props;
    }

    public File getPropsFile()
    {
        return propsFile;
    }

    /**
     * This method allows you to retreive the static copy of the database
     * interface that is being used.
     */
    public static DatabaseInterface getDatabaseInterface()
    {
        return di;
    }

    /**
     * Constructor - this attempts to read the file provided by the filename
     * passed in, using a {@link java.util.Properties Properties}object.
     */
    PropertiesManager(String propertiesFile) throws Exception
    {
        propsFile = new File(propertiesFile);
        props = new Properties();
        props.load(new FileInputStream(propsFile));
    }

    /**
     * This method is used to write the properties back to the property file.
     */
    void saveGlobalProperties() throws Exception
    {
        props.store(new FileOutputStream(propsFile), "");
    }

    /**
     * This method is used to obtain a value for the given property key.
     * 
     * @param key
     *            the key to the value that you are requesting.
     */
    public String getValue(String key)
    {
        return props.getProperty(key);
    }

    /**
     * This method is used to set a value for a given property key.
     */
    public void setValue(String key, String value) throws Exception
    {
        String oldValue = props.getProperty(key);
        props.setProperty(key, value);
        saveGlobalProperties();
        firePropertyChanged(key, value, oldValue);
    }

    /**
     * This method is used to propagate the PropertyChanged event to all of the
     * event listeners that are listening for a property change.
     */
    void firePropertyChanged(String key, String newValue, String oldValue)
    {
        for (int i = 0; i < propertyChangeListeners.size(); i++)
        {
            PropertyChangeListener listener = (PropertyChangeListener) propertyChangeListeners
                    .get(i);
            PropertyChangeEvent event = new PropertyChangeEvent(this, key,
                    newValue, oldValue);
            listener.propertyChange(event);
        }
    }

    /**
     * This method is used to add a PropertyChangeListener object to this
     * object.
     */
    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        propertyChangeListeners.add(listener);
    }

    /**
     * This method will remove a PropertyChangeListener object from this object.
     */
    public void removePropertyChangeListener(PropertyChangeListener listener)
    {
        propertyChangeListeners.remove(listener);
    }

    /**
     * Static function - used to get the global PropertiesManager object
     */
    public static PropertiesManager getPropertiesManager() throws Exception
    {
        if (propsManager == null)
        {
            propsManager = new PropertiesManager("conf/sam.properties");
        }
        return propsManager;
    }

    public void write()
    {
        try
        {
            //open the file for writing, but don't append to the end of the
            // file; start over.
            FileOutputStream fos = new FileOutputStream(getPropsFile(), false);
            for (Enumeration e = getProperties().keys(); e.hasMoreElements();)
            {
                String key = e.nextElement().toString();
                String output = key + "=" + getProperties().getProperty(key)
                        + "\n";
                fos.write(output.getBytes());
            }
            fos.close();
        } catch (Exception e)
        {

        }

    }
}