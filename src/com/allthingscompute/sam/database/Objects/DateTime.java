/*
 * DateTime.java
 *
 * Created on March 2, 2004, 11:49 AM
 */

package com.allthingscompute.sam.database.Objects;

/**
 * 
 * @author intere
 */
public class DateTime
{
    private String date = null;

    private String time = null;

    private String raw = null;

    /** Creates a new instance of DateTime */
    public DateTime()
    {
    }

    public DateTime(DateTime dt)
    {
        copy(dt);
    }

    public DateTime(String raw)
    {
        this.raw = raw;
        parse();
    }

    private void parse()
    {
        String[] parts = this.raw.split(" ");
        String[] dateParts = parts[0].split("-");
        this.date = dateParts[1] + "/" + dateParts[2] + "/" + dateParts[0];
        this.time = parts[1];
    }

    public String getDate()
    {
        return date;
    }

    public String getTime()
    {
        return time;
    }

    public String getBoth()
    {
        return date + " " + time;
    }

    public String toString()
    {
        return getBoth();
    }

    private void copy(DateTime dt)
    {
        this.date = dt.date;
        this.raw = dt.raw;
        this.time = dt.time;
    }

}