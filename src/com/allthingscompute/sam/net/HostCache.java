/*
 * HostCache.java
 *
 * Created on March 19, 2004, 1:05 PM
 */

package com.allthingscompute.sam.net;

import java.util.Hashtable;

/**
 * This class is responsible for maintaining a "cache" of IP to HostName
 * Lookups, as well as a HostName to IP "cache" (so you can access either). The
 * inner workings of this class are 2 simultaneous Hashtables. The first one
 * maintains a mapping of an IP address to a Host Name. The second hash
 * maintains a mapping of a Host Name to an IP address.
 * 
 * @author E. Internicola
 */
public class HostCache
{
    private static Hashtable htIpToHostname = new Hashtable(101);

    private static Hashtable htHostnameToIp = new Hashtable(101);

    /**
     * We don't want this class to actually be instantiated.
     */
    protected HostCache()
    {
    }

    /**
     * This method is used to add an entry into the cache. If the IP is already
     * mapped to an existing hostname, then the old entry is removed from both
     * caches before the actual insertion.
     */
    public static synchronized void addHostEntry(String ip, String hostName)
    {
        String oldHostName = (String) htIpToHostname.get(ip);
        if (oldHostName != null)
        {
            htHostnameToIp.remove(oldHostName);
            htIpToHostname.remove(ip);
        }
        htIpToHostname.put(ip, hostName);
        htHostnameToIp.put(hostName, ip);
    }

    /**
     * This method is used to retreive a hostname from the IP to HostName cache.
     */
    public static String getHostName(String ip)
    {
        try
        {
            return htIpToHostname.get(ip).toString();
        } catch (Exception e)
        {
        }
        return null;
    }

    /**
     * This method is used to retreive an IP form the HostName to IP cache.
     */
    public static String getIpAddress(String hostName)
    {
        return htHostnameToIp.get(hostName).toString();
    }

    /**
     * This method is used to search for a cache entry from either cache.
     * 
     * @param ipOrHost -
     *            can be either an IP Address or a HostName.
     */
    public static String get(String ipOrHost)
    {
        String retVal = (String) htIpToHostname.get(ipOrHost);
        if (retVal == null)
        {
            retVal = (String) htHostnameToIp.get(ipOrHost);
        }
        return retVal;
    }

}