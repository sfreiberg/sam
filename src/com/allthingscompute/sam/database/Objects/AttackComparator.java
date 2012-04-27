/*
 * AttackComparator.java
 *
 * Created on March 11, 2004, 11:20 PM
 */

package com.allthingscompute.sam.database.Objects;

import java.util.Comparator;

/**
 * This class tells how
 * 
 * @author E. Internicola
 */
public class AttackComparator implements Comparator
{
    private int cfield = -1;

    private boolean ascend = true;

    public static final int SORT_CID = 0;

    public static final int SORT_SID = 1;

    public static final int SORT_DESTINATION_IP = 2;

    public static final int SORT_DESTINATION_NAME = 3;

    public static final int SORT_SOURCE_IP = 4;

    public static final int SORT_SOURCE_NAME = 5;

    public static final int SORT_SENSOR_DETAIL = 6;

    public static final int SORT_SENSOR_ENCODING = 7;

    public static final int SORT_SENSOR_FILTER = 8;

    //public static final int SORT_SENSOR_HOST_NAME = 7;
    public static final int SORT_SENSOR_INTERFACE = 10;

    public static final int SORT_SENSOR_LAST_CID = 11;

    public static final int SORT_SENSOR_ADDRESS = 12;

    public static final int SORT_SIGNATURE_SIG_CLASS_ID = 13;

    public static final int SORT_SIGNATURE_SIG_ID = 14;

    public static final int SORT_SIGNATURE_NAME = 15;

    public static final int SORT_SIGNATURE_PRIORITY = 16;

    public static final int SORT_SIGNATURE_REV = 17;

    public static final int SORT_SIGNATURE_SIG_SID = 18;

    public static final int SORT_TIMESTAMP = 19;

    public static final int SORT_SENSOR_ID = 20;

    /**
     * Default Constructor.
     */
    public AttackComparator()
    {
    }

    /**
     * Constructor that sets the cfield (ascend is defaulted to true).
     */
    public AttackComparator(int cfield)
    {
        setCField(cfield);
    }

    /**
     * Constructor that sets the cfield and ascend.
     */
    public AttackComparator(int cfield, boolean ascend)
    {
        setCField(cfield);
        setInvert(ascend);
    }

    /**
     * This method allows you to set the cfield property.
     */
    public void setCField(int cfield)
    {
        this.cfield = cfield;
    }

    /**
     * This method allows you to set the ascend property.
     */
    public void setInvert(boolean ascend)
    {
        this.ascend = ascend;
    }

    /**
     * This method will return the value of the ascend property.
     */
    public boolean getInvert()
    {
        return ascend;
    }

    /**
     * This method will return the value of the cfield property.
     */
    public int getCField()
    {
        return cfield;
    }

    public int compare(Object first, Object last)
    {
        int ret = 0;
        Attack a1 = (Attack) first;
        Attack a2 = (Attack) last;

        if (a1 == null && a2 == null)
        {
            ret = 0;
        } else if (a1 == null)
        {
            ret = 1;
        } else if (a2 == null)
        {
            ret = -1;
        } else
        {
            switch (cfield)
            {
            case SORT_CID:
                ret = (int) (a1.getCid().longValue() - a2.getCid().longValue());
                break;
            case SORT_DESTINATION_IP:
                ret = a1.getDestination().getIP().compareTo(
                        a2.getDestination().getIP());
                break;
            case SORT_DESTINATION_NAME:
                ret = a1.getDestination().getCanonical().compareTo(
                        a2.getDestination().getCanonical());
                break;
            case SORT_SOURCE_IP:
                ret = a1.getSource().getIP().compareTo(a2.getSource().getIP());
                break;
            case SORT_SOURCE_NAME:
                ret = a1.getSource().getCanonical().compareTo(
                        a2.getSource().getCanonical());
                break;
            case SORT_SENSOR_ADDRESS:
                ret = a1.getSensor().getHost().getIP().compareTo(
                        a2.getSensor().getHost().getIP());
                break;
            case SORT_SENSOR_DETAIL:
                ret = a1.getSensor().getDetail().compareTo(
                        a2.getSensor().getDetail().toString());
                break;
            case SORT_SENSOR_ENCODING:
                ret = a1.getSensor().getEncoding().compareTo(
                        a2.getSensor().getEncoding());
                break;
            case SORT_SENSOR_FILTER:
                ret = a1.getSensor().getFilter().compareTo(
                        a2.getSensor().toString());
                break;
            case SORT_SENSOR_INTERFACE:
                ret = a1.getSensor().getInterface().compareTo(
                        a2.getSensor().getInterface());
                break;
            case SORT_SENSOR_LAST_CID:
                ret = a1.getSensor().getLastCid().compareTo(
                        a2.getSensor().getLastCid());
                break;
            case SORT_SID:
                ret = a1.getSid().compareTo(a2.getSid());
                break;
            case SORT_SIGNATURE_NAME:
                ret = a1.getSignature().getSigName().compareTo(
                        a2.getSignature().getSigName());
                break;
            case SORT_SIGNATURE_PRIORITY:
                ret = a1.getSignature().getSigPriority().compareTo(
                        a2.getSignature().getSigPriority());
                break;
            case SORT_SIGNATURE_REV:
                ret = a1.getSignature().getSigRev().compareTo(
                        a2.getSignature().getSigRev());
                break;
            case SORT_SIGNATURE_SIG_CLASS_ID:
                ret = a1.getSignature().getSigClassID().compareTo(
                        a2.getSignature().getSigClassID());
                break;
            case SORT_SIGNATURE_SIG_ID:
                ret = a1.getSignature().getSigID().compareTo(
                        a2.getSignature().getSigID());
                break;
            case SORT_SIGNATURE_SIG_SID:
                ret = a1.getSignature().getSigSid().compareTo(
                        a2.getSignature().getSigSid());
                break;
            case SORT_SENSOR_ID:
                ret = a1.getSensor().getSid()
                        .compareTo(a2.getSensor().getSid());
                break;
            case SORT_TIMESTAMP:
                try
                {
                    ret = a1.getTimestamp().toString().compareTo(
                            a2.getTimestamp().toString());
                } catch (Exception e)
                {
                    if (a1.getTimestamp() == null && a2.getTimestamp() == null)
                    {
                        ret = 0;
                    } else if (a1.getTimestamp() == null)
                    {
                        ret = 1;
                    } else
                    {
                        ret = -1;
                    }
                }
                break;
            }
        }

        if (ascend)
        {
            ret = 0 - ret;
        }

        return ret;
    }
}