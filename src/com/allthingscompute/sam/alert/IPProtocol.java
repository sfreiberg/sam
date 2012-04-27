/*
 * Created on Feb 1, 2005
 */
package com.allthingscompute.sam.alert;


/**
 * @author sam
 */
public class IPProtocol
{
    public static final int TCP = 6;

    public static final int UDP = 17;

    public static final int ICMP = 1;

    public static final int UNKNOWN = 0;

    int protocol = UNKNOWN;

    public IPProtocol(int ipProtocol)
    {
        setProtocol(ipProtocol);
    }

    public void setProtocol(int protocol)
    {
        this.protocol = protocol;
    }

    public int getProtocol()
    {
        return protocol;
    }

    public String getShortDescription()
    {
        return IPProtocol.getShortDescription(protocol);
    }

    public static String getShortDescription(int protocol)
    {
        if (protocol == TCP)
            return "TCP";
        else if (protocol == UDP)
            return "UDP";
        else if (protocol == ICMP)
            return "ICMP";
        else
            return "UNKNOWN";
    }
}
