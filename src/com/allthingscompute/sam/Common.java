package com.allthingscompute.sam;

import java.util.Calendar;

/**
 * This class contains some commonly used functions.
 * 
 * @author E. Internicola
 */
public class Common
{
    /**
     * This method will basically just replaces single quote with a pair of
     * single quotes in the string you provide.
     */
    public static String escapeSQL(String strSQL)
    {
        if (strSQL == null)
            return new String("");
        return new String(strSQL.replaceAll("'", "''"));
    }

    /**
     * If the string provided is null, then this method will return the empty
     * string, otherwise it will return the original string passed to it.
     */
    public static String replaceNull(String text)
    {
        if (text == null)
            return "";
        return text;
    }

    /**
     * If the integer provided is null, then this method will return the empty
     * string, otherwise it will return the original int, converted to a string.
     */
    public static String replaceNull(Integer num)
    {
        if (num == null)
            return "";
        return num.toString();
    }

    /**
     * If the string provided is null (or the empty string), then this method
     * will return the HTML non-breaking space: "&amp;nbsp;". Otherwise it will
     * return the original string.
     */
    public static String replaceNullWithSpace(String text)
    {
        if (text == null || text.equals(""))
        {
            return "&nbsp;";
        }
        return text;
    }

    /**
     * If the Integer provided is null, then this method will reutrn the HTML
     * non-breaking space: "&amp;nbsp;". Otherwise it will convert the Integer
     * to a string, and return that string.
     */
    public static String replaceNullWithSpace(Integer num)
    {
        if (num == null)
        {
            return "&nbsp;";
        }
        return num.toString();
    }

    /**
     * This method will return either a Sequel quoted string, or the string
     * "NULL" depending on the value of the data passed in. If the data is a
     * valid string, then it returns a Sequel quoted copy of the string. If the
     * String object is null, then it returns the string: NULL.
     */
    public static String quotedOrNull(String data)
    {
        if (data == null || data.equals(""))
        {
            return "NULL";
        }
        return "'" + escapeSQL(data) + "'";
    }

    /**
     * This method returns the current time, in the desired format.
     */
    public static String getFormattedTime()
    {
        return getFormattedTime(0);
    }

    /**
     * This method will take the current time, and add the parameter: hours to
     * the current time, and then return the formatted time result.
     */
    public static String getFormattedTime(int hours)
    {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR, hours);
        return formatTime(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
    }

    /**
     * Get the current Hour.
     */
    public static Integer getHour()
    {
        return getHour(0);
    }

    /**
     * Get the current hour added to the number of hours passed here.
     */
    public static Integer getHour(int hours)
    {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR, hours);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        if (hour > 12)
        {
            hour -= 12;
        }
        return new Integer(hour);
    }

    /**
     *  
     */
    public static Integer getMinute()
    {
        Calendar c = Calendar.getInstance();
        return new Integer(c.get(Calendar.MINUTE));
    }

    /**
     * This method will format time to the way you want it. (from HH:MM to HH:MM
     * PM).
     */
    public static String formatTime(int hours, int minutes)
    {
        String ft = "";
        if (hours > 12)
        {
            ft += (hours - 12);
        } else
        {
            ft += hours;
        }
        ft += ":";

        if (minutes < 10)
        {
            ft += "0" + minutes;
        } else
        {
            ft += minutes;
        }

        ft += " ";
        if (hours < 12)
        {
            ft += "AM";
        } else
        {
            ft += "PM";
        }
        return ft;
    }

    /**
     * This method will return a date in the format: MM/DD/YYYY.
     */
    public static String getFormattedDate()
    {
        Calendar c = Calendar.getInstance();
        return (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DATE) + "/"
                + c.get(Calendar.YEAR);
    }

    /**
     * This method will return a date in the format: MM/DD/YYYY. It will Add the
     * requested number of days to the
     */
    public static String getFormattedDate(int days)
    {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, days);
        return (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DATE) + "/"
                + c.get(Calendar.YEAR);
    }

    /**
     * This method will convert a date from the format: MM-DD-YYYY or YYYY-MM-DD
     * to the format: MM/DD/YYYY.
     */
    public static String getSequelDate(String oldDate)
    {
        if (oldDate == null || oldDate.equals(""))
        {
            return "";
        }

        String[] parts = oldDate.split("/|-");
        String newDate = "";
        int Year = -1;

        //determine which slice of the date string is the year
        for (int i = 0; i < parts.length; i++)
        {
            if (parts[i].length() == 4)
            {
                Year = i;
                break;
            }
        }

        //if the year is the first element:
        if (Year == 0)
        {
            if (parts[1].length() == 1)
            {
                parts[1] = "0" + parts[1];
            }
            if (parts[2].length() == 1)
            {
                parts[2] = "0" + parts[2];
            }
            newDate = parts[1] + "/" + parts[2] + "/" + parts[0];
        }
        //if the year is the last element:
        else if (Year == 2)
        {
            if (parts[0].length() == 1)
            {
                parts[0] = "0" + parts[0];
            }
            if (parts[1].length() == 1)
            {
                parts[1] = "0" + parts[1];
            }
            newDate = parts[0] + "/" + parts[1] + "/" + parts[2];
        }
        return newDate;
    }

    public static String getSequelTime(String oldDate)
    {
        if (oldDate == null || oldDate.equals(""))
        {
            return "";
        }
        String[] parts = oldDate.split("[- :.]");
        String newDate = parts[1] + "/" + parts[2] + "/" + parts[0];
        String newTime = parts[3] + ":" + parts[4] + ":" + parts[5];
        return newDate + " " + newTime;
    }

    /**
     * This method will return the string "NULL" or the value of the integer (as
     * a string). This is all dependant on the value of the passed integer. A
     * null integer object will return the string value "NULL", and an integer
     * with a value will return the string value of the integer.
     */
    public static String intOrNull(Integer integer)
    {
        if (integer == null)
            return "NULL";
        return integer.toString();
    }

    private static String getNumber(int x)
    {
        if (x < 10)
        {
            return "0" + x;
        }
        return "" + x + "";
    }

    /**
     * Returns either 00, 15, 30, or 45 depending on what the current time is.
     */
    public static String getNearestMinutes()
    {
        int ret = 0;
        int min = getMinute().intValue();
        if (min >= 7 && min < 22)
        {
            ret = 15;
        } else if (min >= 22 && min < 37)
        {
            ret = 30;
        } else if (min >= 37)
        {
            ret = 45;
        }
        return getNumber(ret);
    }

    /**
     *  
     */
    public static String getAMorPM()
    {
        return getAMorPM(0);
    }

    /**
     *  
     */
    public static String getAMorPM(int hours)
    {
        String data = getFormattedTime(hours);
        return data.substring(data.length() - 2, data.length());
    }
}