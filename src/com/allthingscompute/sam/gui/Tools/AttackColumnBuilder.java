/*
 * ColumnBuilder.java
 *
 * Created on March 12, 2004, 12:27 PM
 */
package com.allthingscompute.sam.gui.Tools;

import java.util.Vector;

import com.allthingscompute.sam.database.Objects.Attack;
import com.allthingscompute.sam.database.Objects.AttackVector;

/**
 * This class is used to do all sorts of things related to a
 * 
 * @author E. Internicola
 */
public class AttackColumnBuilder
{
    public static final int MAX_COLUMN_SIZE = 11;

    public static final int COL_ATTACK_TYPE = 0;

    public static final int COL_COUNT_ID = 1;

    public static final int COL_DESTINATION_ADDRESS = 2;

    public static final int COL_DESTINATION_IP = 3;

    public static final int COL_SENSOR_ADDRESS = 4;

    public static final int COL_SENSOR_ID = 5;

    public static final int COL_SENSOR_INTERFACE = 6;

    public static final int COL_SIGNATURE_ID = 7;

    public static final int COL_SOURCE_ADDRESS = 8;

    public static final int COL_SOURCE_IP = 9;

    public static final int COL_TIMESTAMP = 10;

    public static final String STR_ATTACK_TYPE = "Attack Type";

    public static final String STR_COUNT_ID = "Count ID";

    public static final String STR_DESTINATION_ADDRESS = "Destination Address";

    public static final String STR_DESTINATION_IP = "Destination IP";

    public static final String STR_SENSOR_ADDRESS = "Sensor Address";

    public static final String STR_SENSOR_ID = "Sensor ID";

    public static final String STR_SENSOR_INTERFACE = "Sensor Interface";

    public static final String STR_SIGNATURE_ID = "Signature ID";

    public static final String STR_SOURCE_ADDRESS = "Source Address";

    public static final String STR_SOURCE_IP = "Source IP";

    public static final String STR_TIMESTAMP = "Timestamp";

    /**
     * This method will return the integer value that corresponds to the Column
     * Name.
     */
    public static int getIntFromName(String sCol)
    {
        if (sCol.equals(STR_ATTACK_TYPE))
        {
            return COL_ATTACK_TYPE;
        } else if (sCol.equals(STR_COUNT_ID))
        {
            return COL_COUNT_ID;
        } else if (sCol.equals(STR_DESTINATION_ADDRESS))
        {
            return COL_DESTINATION_ADDRESS;
        } else if (sCol.equals(STR_DESTINATION_IP))
        {
            return COL_DESTINATION_IP;
        } else if (sCol.equals(STR_SENSOR_ADDRESS))
        {
            return COL_SENSOR_ADDRESS;
        } else if (sCol.equals(STR_SENSOR_ID))
        {
            return COL_SENSOR_ID;
        } else if (sCol.equals(STR_SENSOR_INTERFACE))
        {
            return COL_SENSOR_INTERFACE;
        } else if (sCol.equals(STR_SIGNATURE_ID))
        {
            return COL_SIGNATURE_ID;
        } else if (sCol.equals(STR_SOURCE_ADDRESS))
        {
            return COL_SOURCE_ADDRESS;
        } else if (sCol.equals(STR_SOURCE_IP))
        {
            return COL_SOURCE_IP;
        } else if (sCol.equals(STR_TIMESTAMP))
        {
            return COL_TIMESTAMP;
        } else
        {
            return -1;
        }
    }

    /**
     * This method will return the Column Name corresponding to the provided
     * column integer enumeration.
     */
    public static String getColumnName(int iCol)
    {
        String col = "";
        switch (iCol)
        {
        case COL_ATTACK_TYPE:
            col = STR_ATTACK_TYPE;
            break;
        case COL_COUNT_ID:
            col = STR_COUNT_ID;
            break;
        case COL_DESTINATION_ADDRESS:
            col = STR_DESTINATION_ADDRESS;
            break;
        case COL_DESTINATION_IP:
            col = STR_DESTINATION_IP;
            break;
        case COL_SENSOR_ADDRESS:
            col = STR_SENSOR_ADDRESS;
            break;
        case COL_SENSOR_ID:
            col = STR_SENSOR_ID;
            break;
        case COL_SENSOR_INTERFACE:
            col = STR_SENSOR_INTERFACE;
            break;
        case COL_SIGNATURE_ID:
            col = STR_SIGNATURE_ID;
            break;
        case COL_SOURCE_ADDRESS:
            col = STR_SOURCE_ADDRESS;
            break;
        case COL_SOURCE_IP:
            col = STR_SOURCE_IP;
            break;
        case COL_TIMESTAMP:
            col = STR_TIMESTAMP;
            break;
        }
        return col;
    }

    /**
     * This method is responsible for taking an AttackVector and a Vector (of
     * ints) and creating a Vector of Vectors, that can be used to be displayed
     * by a JTable.
     */
    public static Vector displayColumns(AttackVector av, Vector vColumns)
    {
        Vector vTableData = new Vector();
        for (int i = 0; i < av.size(); i++)
        {
            Vector v = new Vector(vColumns.size());
            Attack a = (Attack) av.elementAt(i);
            for (int j = 0; j < vColumns.size(); j++)
            {
                int col = Integer.parseInt(vColumns.elementAt(j).toString());
                switch (col)
                {
                case COL_ATTACK_TYPE:
                    v.add(a.getSignature().getSigName().toString());
                    break;
                case COL_COUNT_ID:
                    v.add(a.getCid().toString());
                    break;
                case COL_DESTINATION_ADDRESS:
                    v.add(a.getDestination().getCanonical().toString());
                    break;
                case COL_DESTINATION_IP:
                    v.add(a.getDestination().getIP().toString());
                    break;
                case COL_SENSOR_ADDRESS:
                    v.add(a.getSensor().getHost().getIP());
                    break;
                case COL_SENSOR_ID:
                    v.add(a.getSid().toString());
                    break;
                case COL_SENSOR_INTERFACE:
                    v.add(a.getSensor().getInterface().toString());
                    break;
                case COL_SIGNATURE_ID:
                    v.add(a.getSignature().getSigID().toString());
                    break;
                case COL_SOURCE_ADDRESS:
                    v.add(a.getSource().getCanonical().toString());
                    break;
                case COL_SOURCE_IP:
                    v.add(a.getSource().getIP().toString());
                    break;
                case COL_TIMESTAMP:
                    v.add(a.getTimestamp().toString());
                    break;
                }
            }
            vTableData.add(v);
        }
        return vTableData;
    }

    /**
     * This method will take a Vector of ints (from the range of values
     * 0-MAX_COLUMN_SIZE) and return a Vector of Strings that are the actual
     * Column headings for the given int representations of them.
     */
    public static Vector getNamesFromInts(Vector iCols)
    {
        Vector sCols = new Vector();
        for (int i = 0; i < iCols.size(); i++)
        {
            sCols.add(getColumnName(Integer.parseInt(iCols.elementAt(i)
                    .toString())));
        }
        return sCols;
    }

    /**
     * This method will create a Vector of Integer objects from the provided int
     * array.
     */
    public static Vector getIntVectorFromInts(int[] iCols)
    {
        Vector vCols = new Vector();
        for (int i = 0; i < iCols.length; i++)
        {
            vCols.add(new Integer(iCols[i]));
        }
        return vCols;
    }

    /**
     * This method will Intersect the intVector you provide with the Universal
     * Set (All of the possible Columns). It returns a Vector of Integer
     * Objects.
     */
    public static Vector intersectWithUniversalSet(Vector intVector)
    {
        Vector intersection = new Vector();
        int[] all = getAllColumnsAsInts();
        for (int i = 0; i < all.length; i++)
        {
            boolean add = true;
            for (int j = 0; j < intVector.size(); j++)
            {
                int k = Integer.parseInt(intVector.elementAt(j).toString());
                if (k == all[i])
                {
                    add = false;
                }
            }
            if (add)
            {
                intersection.add(new Integer(all[i]));
            }
        }
        return intersection;
    }

    /**
     * This method will return an array of Column Titles that pertains to all of
     * the available Column Headings.
     */
    public static String[] getAllColumnsAsStrings()
    {
        String[] cols = new String[MAX_COLUMN_SIZE];
        for (int i = 0; i < MAX_COLUMN_SIZE; i++)
        {
            cols[i] = getColumnName(i);
        }
        return cols;
    }

    /**
     * This method will convert a Vector of Integer objects to a comma separated
     * list of ints (as a String object).
     */
    public static String intVectorToString(Vector v)
    {
        String values = "";
        for (int i = 0; i < v.size(); i++)
        {
            if (i != 0)
            {
                values += ",";
            }
            Integer j = new Integer(v.elementAt(i).toString());
            values += j.toString();
        }
        return values;
    }

    /**
     * This method will take a list of integers (separated by a comma), and
     * create a Vector of Integer objects from the list.
     */
    public static Vector stringToIntVector(String list)
    {
        Vector v = new Vector();
        try
        {
            String[] parts = list.split(",");
            for (int i = 0; i < parts.length; i++)
            {
                v.add(new Integer(parts[i]));
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Exception converting Int Vector to Strings:\t"
                    + e);
        }
        return v;
    }

    public static int[] stringToIntArray(String list)
    {
        int[] iList = null;
        try
        {
            String[] parts = list.split(",");
            iList = new int[parts.length];
            for (int i = 0; i < parts.length; i++)
            {
                iList[i] = Integer.parseInt(parts[i]);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Exception converting String to int Array:\t"
                    + e);
        }
        return iList;
    }

    /**
     * This method will return an array of Strings that pertains to the provided
     * int column heading enumerations.
     */
    public static String[] getColumnNames(int[] cols)
    {
        String[] strCols = new String[cols.length];
        for (int i = 0; i < cols.length; i++)
        {
            strCols[i] = getColumnName(cols[i]);
        }
        return strCols;
    }

    /**
     * This method will return an Array of ints, which pertains to the entire
     * enumeration of columns available.
     */
    public static int[] getAllColumnsAsInts()
    {
        int[] cols = new int[MAX_COLUMN_SIZE];
        for (int i = 0; i < MAX_COLUMN_SIZE; i++)
        {
            cols[i] = i;
        }
        return cols;
    }
}