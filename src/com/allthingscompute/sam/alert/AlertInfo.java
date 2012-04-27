package com.allthingscompute.sam.alert;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * This class contains various information about an Alert.
 * 
 * @author sfreiberg Date Aug 21, 2002
 */
public class AlertInfo
{
    /** The Alert ID for this alert. */
    private String alertId;

    /** The Source Address for this alert. */
    private InetAddress sourceAddress;

    /** The Destination Address for this alert. */
    private InetAddress destinationAddress;

    /** The Source Port for this alert. */
    private String sourcePort;

    /** The Destination Port for this alert. */
    private String destinationPort;

    /**
     * Constructor that takes all parameters as String objects. This constructor
     * uses the InetAddress.getByName method to lookup the source and
     * destination addresses that are provided. If a host address is unable to
     * be found, then a UnknownHostException will be raised.
     */
    public AlertInfo(String alertId, String sourceAddress,
            String destinationAddress, String sourcePort, String destinationPort)
            throws UnknownHostException
    {
        // before passing the data off to the other constructor, convert the
        // source and destination address strings to InetAddress objects
        // (which could potentially raise an UnknownHostException)
        this(alertId, InetAddress.getByName(sourceAddress), //convert Source
                                                            // Address to an
                                                            // InetAddress
                                                            // Object
                InetAddress.getByName(destinationAddress), //convert
                                                           // Destination
                                                           // Address to an
                                                           // InetAddress Object
                sourcePort, destinationPort);
    }

    /**
     * Constructor that takes all parameters as strings, excluding Addresses;
     * they are taken as InetAddress Objects. The internal data is then set by
     * this constructor. Data can then be accessed by the documented public
     * methods.
     * 
     * @see #AlertInfo(String,String,String,String,String)
     */
    public AlertInfo(String alertId, InetAddress sourceAddress,
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