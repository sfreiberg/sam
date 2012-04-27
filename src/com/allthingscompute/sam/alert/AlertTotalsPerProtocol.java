/*
 * Created on Feb 1, 2005
 */
package com.allthingscompute.sam.alert;


/**
 * @author sam
 */
public class AlertTotalsPerProtocol
{
    int icmp = 0;

    int udp = 0;

    int tcp = 0;

    int unknown = 0;

    public void setTotal(int ipProtocol, int total)
    {
        if (ipProtocol == IPProtocol.ICMP)
            setIcmp(total);
        else if (ipProtocol == IPProtocol.UDP)
            setUdp(total);
        else if (ipProtocol == IPProtocol.TCP)
            setTcp(total);
        else
            setUnknown(total);
    }

    /**
     * @return Returns the icmp.
     */
    public int getIcmp()
    {
        return icmp;
    }

    /**
     * @param icmp
     *            The icmp to set.
     */
    public void setIcmp(int icmp)
    {
        this.icmp = icmp;
    }

    /**
     * @return Returns the tcp.
     */
    public int getTcp()
    {
        return tcp;
    }

    /**
     * @param tcp
     *            The tcp to set.
     */
    public void setTcp(int tcp)
    {
        this.tcp = tcp;
    }

    /**
     * @return Returns the udp.
     */
    public int getUdp()
    {
        return udp;
    }

    /**
     * @param udp
     *            The udp to set.
     */
    public void setUdp(int udp)
    {
        this.udp = udp;
    }

    /**
     * @return Returns the unknown.
     */
    public int getUnknown()
    {
        return unknown;
    }

    /**
     * @param unknown
     *            The unknown to set.
     */
    public void setUnknown(int unknown)
    {
        this.unknown = unknown;
    }
}
