/*
 * AttackFilter.java
 *
 * Created on March 4, 2004, 8:33 PM
 */

package com.allthingscompute.sam.database.Filter;

/**
 * This class is used to maintain all of the information about a filter. A
 * filter can be used to filter out certian attacks (
 * {@link #FILTER_HIDE FILTER_HIDE}), or to show only certian attacks (
 * {@link #FILTER_SHOW FILTER_SHOW}).
 * 
 * @author E. Internicola
 */
public class AttackFilter
{
    public static final int FIELD_SENSOR_INTERFACE = 0;

    public static final int FIELD_SENSOR_ADDRESS = 1;

    public static final int FIELD_SOURCE = 2;

    public static final int FIELD_DESTINATION = 3;

    public static final int FIELD_ATTACK_SIGNATURE = 4;

    public static final int FIELD_TIMESTAMP = 5;

    public static final int FILTER_SHOW = 0;

    public static final int FILTER_HIDE = 1;

    public static final int FILTER_TYPE_USER = 0;

    public static final int FILTER_TYPE_SYSTEM = 1;

    private int filterField;

    private String filterValue = null;

    private int filterType;

    private int accessType;

    /**
     * Constructor that will set all of the provided parameters, and set the
     * access type property to FILTER_TYPE_USER.
     */
    public AttackFilter(int filterField, String filterValue, int filterType)
    {
        this(filterField, filterValue, filterType, FILTER_TYPE_USER);
    }

    /** Constructor that will set all of the provided parameters. */
    public AttackFilter(int filterField, String filterValue, int filterType,
            int accessType)
    {
        setFilterField(filterField);
        setFilterValue(filterValue);
        setFilterType(filterType);
        setAccessType(accessType);
    }

    /** This method will set the access type property. */
    public void setAccessType(int accessType)
    {
        if (accessType < FILTER_TYPE_USER || accessType > FILTER_TYPE_SYSTEM)
        {
            new Exception("Invalid access type:\t" + accessType);
            return;
        }
        this.accessType = accessType;
    }

    /** This method will set the filter field property. */
    public void setFilterField(int filterField)
    {
        this.filterField = filterField;
    }

    /** This method will set the filter Value property. */
    public void setFilterValue(String filterValue)
    {
        this.filterValue = filterValue;
    }

    /** This method will set the Filter Type property. */
    public void setFilterType(int filterType)
    {
        this.filterType = filterType;
    }

    /** This method will return the filter field. */
    public int getFilterField()
    {
        return filterField;
    }

    /** This method will return the filter type. */
    public int getFilterType()
    {
        return filterType;
    }

    /** This method will return the filter value. */
    public String getFilterValue()
    {
        return filterValue;
    }

    /** This method will return the access type. */
    public int getAccessType()
    {
        return accessType;
    }
}