package com.allthingscompute.sam.alert;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * This class contains various information about an Alert. It seems to be the
 * same thing as {@link com.allthingscompute.sam.alert.AlertInfo AlertInfo}but
 * I am merely documenting this, so perhaps I'll find the reason for making the
 * same code over again? I honestly don't know.
 * 
 * @author Documented by E. Internicola
 * @author sfreiberg Date Aug 21, 2002
 */
public class AlertInfo2
{
    /** The Alert ID of this alert. */
    private String alertId;

    /** The Source Address of this alert. */
    private InetAddress sourceAddress;

    /** The Destination Address of this alert. */
    private InetAddress destinationAddress;

    /** The Source Port of this alert. */
    private String sourcePort;

    /** The Destination Port of this alert. */
    private String destinationPort;

    /**
     * Constructor that takes all parameters as String objects. This constructor
     * uses the InetAddress.getByName method to lookup the source and
     * destination addresses that are provided. If a host address is unable to
     * be found, then a UnknownHostException will be raised.
     */
    public AlertInfo2(String alertId, String sourceAddress,
            String destinationAddress, String sourcePort, String destinationPort)
            throws UnknownHostException
    {
        this(alertId, InetAddress.getByName(sourceAddress), InetAddress
                .getByName(destinationAddress), sourcePort, destinationPort);
    }

    /**
     * Constructor that takes all parameters as strings, excluding Addresses;
     * they are taken as InetAddress Objects. The internal data is then set by
     * this constructor. Data can then be accessed by the documented public
     * methods.
     * 
     * @see #AlertInfo2(String,String,String,String,String)
     */
    public AlertInfo2(String alertId, InetAddress sourceAddress,
            InetAddress destinationAddress, String sourcePort,
            String destinationPort)
    {
        this.alertId = alertId;
        this.sourceAddress = sourceAddress;
        this.destinationAddress = destinationAddress;
        this.sourcePort = sourcePort;
        this.destinationPort = destinationPort;
    }

    /** Method to retreive the AlertID. */
    public String getAlertId()
    {
        return alertId;
    }

    /** Method to set the AlertID. */
    public void setAlertId(String id)
    {
        alertId = id;
    }

    /** Method to retreive the Source Address. */
    public InetAddress getSourceAddress()
    {
        return sourceAddress;
    }

    /** Method to set the Source Address. */
    public void setSourceAddress(InetAddress address)
    {
        sourceAddress = address;
    }

    /** Method to get the Source Port. */
    public String getSourcePort()
    {
        return sourcePort;
    }

    /** Method to set the Source Port. */
    public void setSourcePort(String port)
    {
        sourcePort = port;
    }

    /** Method to get the Destination Address. */
    public InetAddress getDestinationAddress()
    {
        return destinationAddress;
    }

    /** Method to set the Destination Address. */
    public void setDestinationAddress(InetAddress address)
    {
        destinationAddress = address;
    }

    /** Method to get the Destination Port. */
    public String getDestinationPort()
    {
        return destinationPort;
    }

    /** Method to set the Destination Port */
    public void setDestinationPort(String port)
    {
        destinationPort = port;
    }

}