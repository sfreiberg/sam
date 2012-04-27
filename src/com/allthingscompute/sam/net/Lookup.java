
package com.allthingscompute.sam.net;

import java.net.InetAddress;

import com.allthingscompute.sam.config.PropertiesManager;

/**
 * Lookup class - this will take a long integer, and convert it to a dotted
 * decimal formed IP address. Then it actually does a lookup to determine the
 * canonical name for the given IP address.
 * 
 * @author E. Internicola
 */
public class Lookup
{
    private long decimal;

    private int[] ddfi = null;

    private int[] binary = null;

    private String ddfs = null;

    private String canonical = null;

    private InetAddress ia = null;

    private Boolean overrideLookup = new Boolean(false);

    private static Double failures = null;

    private static Double nonFailures = null;

    private static Double threshhold = null;

    private static Boolean disableLookup = null;

    private static void setFailures(Double dblFailures)
    {
        failures = dblFailures;
    }

    private static void setNonFailures(Double dblNonFailures)
    {
        nonFailures = dblNonFailures;
    }

    private static Double getFailures()
    {
        if (failures == null)
        {
            failures = new Double(0.0);
        }
        return failures;
    }

    private static Double getNonFailures()
    {
        if (nonFailures == null)
        {
            nonFailures = new Double(0.0);
        }
        return nonFailures;
    }

    private static void incrementFailures()
    {
        setFailures(new Double(getFailures().doubleValue() + 1));
    }

    private static void incrementNonFailures()
    {
        setNonFailures(new Double(getNonFailures().doubleValue() + 1));
    }

    /**
     * This method sets the value of the Override Lookup property.
     */
    public void setOverrideLookup(boolean overrideLookup)
    {
        this.overrideLookup = new Boolean(overrideLookup);
    }

    /**
     * This method sets the overrideLookup to true, and makes a call to do the
     * Lookup.
     */
    public void executeLookup()
    {
        if (canonical != null && ddfs != null && canonical.equals(ddfs))
        {
            setOverrideLookup(true);
            doLookup();
        }
    }

    /**
     * This method returns the value of override lookup.
     */
    private boolean getOverrideLookup()
    {
        return overrideLookup.booleanValue();
    }

    /**
     * This method loads the threshhold if it isn't loaded yet, and returns the
     * value of the threshhold.
     */
    private static double getThreshhold()
    {
        //if( threshhold == null ) {
        loadThreshhold();
        //}
        return threshhold.doubleValue();
    }

    /**
     * This method loads the disable lookup if it isn't loaded yet, and returns
     * the value of the disableLookup.
     */
    private boolean getDisableLookup()
    {
        //if( disableLookup == null ) {
        loadDisableLookup();
        //}
        return disableLookup.booleanValue();
    }

    private static void setThreshhold(Double dblThreshhold)
    {
        try
        {
            threshhold = dblThreshhold;
            PropertiesManager.getPropertiesManager().setValue(
                    "Lookup-Threshhold", threshhold.toString());
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Exception storing threshhold:\t" + e);
        }
    }

    /**
     * This method will load the "Lookup-Threshhold" property from the
     * properties manager, or default to "0.5".
     */
    private static void loadThreshhold()
    {
        try
        {
            PropertiesManager pm = PropertiesManager.getPropertiesManager();
            threshhold = new Double(pm.getValue("Lookup-Threshhold").toString());
        } catch (Exception e)
        {
            setThreshhold(new Double("0.50"));
        }
    }

    private static void setDisableLookup(Boolean blnDisableLookup)
    {
        try
        {
            disableLookup = blnDisableLookup;
            PropertiesManager.getPropertiesManager().setValue("DisableLookup",
                    disableLookup.toString());
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Exception setting Disable Lookup property:\t"
                    + e);
        }
    }

    /**
     * This method will load the "DisableLookup" property from the properties
     * manager, or default to false.
     */
    private static void loadDisableLookup()
    {
        try
        {
            PropertiesManager pm = PropertiesManager.getPropertiesManager();
            disableLookup = new Boolean(pm.getValue("DisableLookup").toString());
        } catch (Exception e)
        {
            setDisableLookup(new Boolean(false));
        }
    }

    /**
     * This method determines wether or not a lookup should take place.
     */
    private boolean shouldILookup()
    {
        double fRate, total;
        if (getOverrideLookup())
            return true;
        else if (getDisableLookup())
            return false;
        try
        {
            total = getNonFailures().doubleValue()
                    + getFailures().doubleValue();
            fRate = getFailures().doubleValue() / total;
            if (fRate > getThreshhold() && total > 10)
            {
                return false;
            }
            System.out.println("falure rate:\t" + fRate);
        } catch (Exception e)
        {
            System.out.println("Division by zero");
        }
        return true;
    }

    /**
     * Constructor
     */
    public Lookup(long Decimal)
    {
        decimal = Decimal;
        calculate();
    }

    public InetAddress getInetAddress()
    {
        return ia;
    }

    /**
     *  
     */
    public Lookup(String decimal)
    {
        try
        {
            Long ltemp = new Long(decimal);
            this.decimal = ltemp.longValue();
            calculate();
            return;
        } catch (Exception e)
        {
            this.ddfs = decimal;
            doLookup();
        }
    }

    /**
     * This method executes the lookup.
     */
    public void doLookup()
    {
        try
        {
            canonical = HostCache.getHostName(ddfs);
            if (canonical != null)
            {
                return;
            }
            if (shouldILookup())
            {
                ia = InetAddress.getByName(ddfs);
                canonical = ia.getCanonicalHostName();
                ddfs = ia.getHostAddress();
                if (ia.getCanonicalHostName().equals(ia.getHostAddress()))
                {
                    incrementFailures();
                } else
                {
                    incrementNonFailures();
                    HostCache.addHostEntry(ddfs, canonical);
                }
            } else
            {
                canonical = ddfs;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Exception creating Lookup Object:\t" + e);
        }

    }

    /**
     * This method calcualtes the Dotted Decimal IP address.
     */
    private void calculate()
    {
        binary = new int[32];
        ddfi = new int[4];
        ddfs = "";
        long value;
        long working = decimal;
        int counter = 0;
        int count = 0;
        int index = 3;
        //first calculate the binary value
        for (int i = 31; i > -1; --i)
        {
            value = (long) Math.pow(2, i);
            if (working >= value)
            {
                working -= value;
                binary[31 - i] = 1;
                count += (int) Math.pow(2, i % 8);
            } else
            {
                binary[31 - i] = 0;
            }
            counter++;

            //now do bookkeeping for the Dotted Decimal IP
            if (counter == 8)
            {
                if (index != 3)
                {
                    ddfs += ".";
                }
                ddfs += count;
                ddfi[index] = count;
                counter = 0;
                count = 0;
                index--;
            }
        }
        doLookup();
    }

    /**
     * This method returns the Canonical Name.
     */
    public String getCanonical()
    {
        return canonical;
    }

    /**
     * This method will take a host name as a string, and attempt to create an
     * instance of an InetAddress object from it (because a canonical name might
     * be supplied), then convert its IP to a long value. The
     * InetAddress.hashCode method fails here because it overflows into a
     * negative value, so we need a long to hold the entire value. Perhaps there
     * is some way to convert an int to a long and account for the overflow, but
     * I'm unaware of it thus far, so I used a little bit of mathematics to do
     * the conversion.
     */
    public long getIPHash()
    {
        try
        {
            String chunk = null;
            String[] parts = null;
            if (ddfs != null)
            {
                chunk = ddfs;
            } else if (canonical != null)
            {
                chunk = HostCache.getIpAddress(canonical);
            }

            if (chunk != null)
            {
                parts = chunk.split("\\.");
            } else if (shouldILookup())
            {
                parts = ia.getHostAddress().split("\\.");
            } else
            {
                parts = ddfs.split("\\.");
            }
            long value = 0;
            long part = Long.parseLong(parts[0]);
            value += part * (long) Math.pow((double) 256, (double) 3);
            part = Long.parseLong(parts[1]);
            value += part * (long) Math.pow((double) 256, (double) 2);
            part = Long.parseLong(parts[2]);
            value += part * 256;
            part = Long.parseLong(parts[3]);
            value += part;
            return value;
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Exception getting the IP Hash");
        }
        return -1;
    }

    /**
     * Returns a dotted decimal form string that represents the IP address of
     * the given ip.
     */
    public String getIP()
    {
        return ddfs;
    }

    /**
     *  
     */
    public String toString()
    {
        if (canonical == null)
            return ddfs;
        return canonical;
    }

}