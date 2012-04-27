
package com.allthingscompute.sam.database;

/**
 * This interface defines the methods that <u>must </u> be implemented by a
 * Database Interface class in order to make SAM function correctly for the
 * given type of database.
 * 
 * @author E. Internicola
 */
public interface DatabaseInterface
{
    /**
     * This method will return a SQL string that will retreive the number of
     * attacks in the last N minutes from the event table. You must leave a
     * question mark (?) in the query in place of where the minutes would go, as
     * a prepared statement is created using the JDBC, and the question mark is
     * replaced with 5, 15, 30, and 60 for the cooresponding queries.
     */
    public String getNumOfAttacks();

    /**
     * This method will return a SQL string that will retreive the total number
     * of attacks from the event table.
     */
    public String getNumOfAttacksForever();

    /**
     * This method will return a SQL string that will retreive the current time
     * (from the database).
     */
    public String getCurDatabaseTime();

    /**
     * This method will return a SQL string that will retreive the top 15
     * sources of *attacks* in the last 24 hours, ordered by number of attacks.
     */
    public String getTopAttackers();

    /**
     * This method will return a SQL string that will retreive the top 15
     * *attacks* in the last 24 hours, ordered by number of attacks.
     */
    public String getTopAttacks();

    /**
     * This method will return a SQL string that will retreive the number of
     * attacks in the last 5 minutes.
     */
    public String getAttacksLastHour();

    /**
     * This method will return a SQL string that will retreive the source and
     * destination ports for a given event (denoted by its' cid value which is
     * held by a question mark).
     */
    public String getPortsTCP();

    /**
     * This method will return a SQL string that will retreive the source and
     * destination ports for a given event (denoted by it's cid value which is
     * held by a question mark).
     */
    public String getPortsUDP();

    /**
     * This method will return a SQL String that will retreive a result set of
     * all of the Attacks matching the specified type of attack in the past 24
     * hours.
     */
    public String getRelatedAttacks(String attackType, long hours);

    /**
     * This method will return a SQL String that will retreive a result set of
     * all of the Attacks matching the specified type of attack in the past 24
     * hours.
     */
    public String getRelatedSources(long source, long hours);

    /**
     * This method will return a SQL String that will retreive a result set of
     * all of the Attacks matching the specified type of attack in the past n
     * hours.
     */
    public String getRelatedDestinations(long dest, long hours);

    /**
     * This method will return a SQL String that (when executed) will purge all
     * data in the EVENT Table before the given date. During a purge, you must
     * first make sure to use all of the other Purge calls first:
     * <ul>
     * <li>{@link #getPurgeICMP getPurgeICMP()}</li>
     * <li>{@link #getPurgeIP getPurgeIP()}</li>
     * <li>{@link #getPurgeTCP getPurgeTCP()}</li>
     * <li>{@link #getPurgeUDP getPurgeUDP()}</li>
     * <li>{@link #getPurgeData getPurgeData()}</li>
     * </ul>
     */
    public String getPurgeEvents();

    /**
     * This method will return a SQL String that (when executed) will purge all
     * data in the ICMPHDR Table before the given date (using the timestamp from
     * the events table).
     */
    public String getPurgeICMP();

    /**
     * This method will return a SQL String that (when executed) will purge all
     * data in the IPHDR Table before the given date (using the timestamp from
     * the events table).
     */
    public String getPurgeIP();

    /**
     * This method will return a SQL String that (when executed) will purge all
     * data in the TCPHDR Table before the given date (using the timestamp from
     * the events table).
     */
    public String getPurgeTCP();

    /**
     * This method will return a SQL String that (when executed) will purge all
     * data in the UDPHDR Table before the given date (using the timestamp from
     * the events table).
     */
    public String getPurgeUDP();

    /**
     * This method will return a SQL String that (when executed) will purge all
     * data in the DATA Table before the given date (using the timestamp from
     * the events table).
     */
    public String getPurgeData();

    /**
     * This method will return a SQL String that (when executed) will purge all
     * data in the OPT Table before the given data (using the timestamp from the
     * events table).
     */
    public String getPurgeOpt();

    /**
     * This method will return a SQL String that (when executed) will purge all
     * data in the SIGNATURE Table before the given data (using the timestamp
     * from the events table).
     */
    public String getPurgeSignature();

    /**
     * This method will return an SQL String that (when executed) will return a
     * result set containing the data for the given packet.
     */
    public String getData(long sid, long cid);

    /**
     * This method will return an SQL String that (when executed) will return a
     * result set containing the data for the given attack cid.
     */
    public String getSignature(long sid, long cid);
    
    /**
     * This method will return an SQL String that (when executed) will return a
     * result set containing the total alerts by severity
     * 
     * added 1/24/2005 Sam Freiberg
     */
    public String getAlertsBySeverity();
    
    /**
     * This method will return an SQL String that (when executed) will return a
     * result set containing the total alerts by protocol
     * 
     * added 1/24/2005 Sam Freiberg
     */
    public String getAlertsByProtocol();
}