
package com.allthingscompute.sam.database;

/**
 * This class is a SQL database Interface to postgresql. It defines the strings
 * to be used to do various SAM related selects for a PostgreSQL snort database.
 * (Postgresql Version: 7.2.3)
 * 
 * @author E. Internicola <BR>
 *         Date 05/05/2003
 * @see DatabaseInterface
 */
public class PostgreSQLInterface implements DatabaseInterface
{

    public String getNumOfAttacks()
    {
        return "SELECT COUNT(*) FROM Event WHERE ( EXTRACT( EPOCH FROM (Now() - Timestamp) ) ) <= 60*?";
    }

    public String getNumOfAttacksForever()
    {
        return "SELECT COUNT(*) FROM event";
    }

    public String getCurDatabaseTime()
    {
        return "SELECT CURRENT_TIMESTAMP";
    }

    public String getTopAttackers()
    {
        return "SELECT DISTINCT iphdr.ip_src AS ip_addr, COUNT(event.cid) AS num_events FROM event "
                + "LEFT JOIN iphdr ON ((event.sid=iphdr.sid) AND (event.cid=iphdr.cid)) WHERE (ip_src > -1) AND "
                + "EXTRACT( EPOCH FROM (Now() - Event.timestamp) ) <= (60 * 60 * 24)  GROUP BY ip_addr ORDER BY num_events DESC LIMIT 15";
    }

    public String getTopAttacks()
    {
        return "SELECT signature.sig_name, count(signature) as sig_cnt FROM event LEFT JOIN signature ON "
                + "event.signature=signature.sig_id WHERE event.cid > 0 AND "
                + "(EXTRACT( EPOCH FROM (Now()  - Event.timestamp) ) <= (60 * 60 * 24) ) "
                + "GROUP BY signature.sig_name ORDER BY sig_cnt DESC LIMIT 15";
    }

    public String getAttacksLastHour()
    {
        return "SELECT S.*, SE.*, E.*, I.* FROM Sensor SE, Signature S, "
                + "Event E LEFT JOIN Iphdr I ON E.sid=I.sid AND E.cid=I.cid "
                + "WHERE E.cid > 0 AND SE.sid=E.sid "
                + "AND EXTRACT( EPOCH FROM (Now() -  E.Timestamp) ) <= (60*60) "
                + "AND E.signature=S.sig_id";
    }

    public String getPortsTCP()
    {
        return "SELECT * FROM tcphdr WHERE cid=?";
    }

    public String getPortsUDP()
    {
        return "SELECT * FROM udphdr WHERE cid=?";
    }

    public String getRelatedAttacks(String attackType, long hours)
    {
        String strSql = "SELECT S.*, SE.*, E.*, I.* FROM Sensor SE, Signature S, Event E LEFT JOIN IPHDR I ON "
                + "E.sid=I.sid AND E.cid=I.cid WHERE E.cid > 0 "
                + "AND EXTRACT( EPOCH FROM (Now() -  E.Timestamp) ) <= (60*60*"
                + hours
                + ") "
                + "AND S.sig_name='"
                + attackType.replaceAll("'", "''")
                + "' AND SE.sid=E.sid "
                + "AND E.signature=S.sig_id";
        return strSql;
    }

    public String getRelatedSources(long source, long hours)
    {
        String strSql = "SELECT S.*, SE.*, E.*, I.* FROM Sensor SE, Signature S, Event E LEFT JOIN IPHDR I ON "
                + "E.sid=I.sid AND E.cid=I.cid WHERE E.cid > 0 "
                + "AND EXTRACT( EPOCH FROM (Now() -  E.Timestamp) ) <= (60*60*"
                + hours
                + ") "
                + "AND I.ip_src="
                + source
                + " AND SE.sid=E.sid " + "AND E.signature=S.sig_id";
        return strSql;
    }

    public String getRelatedDestinations(long dest, long hours)
    {
        String strSql = "SELECT S.*, SE.*, E.*, I.* FROM Sensor SE, Signature S, Event E LEFT JOIN IPHDR I ON "
                + "E.sid=I.sid AND E.cid=I.cid WHERE E.cid > 0 AND SE.sid=E.sid "
                + "AND EXTRACT( EPOCH FROM (Now() -  E.Timestamp) ) <= (60*60*"
                + hours
                + ") "
                + "AND I.ip_dst="
                + dest
                + " "
                + "AND E.signature=S.sig_id";
        return strSql;
    }

    public String getPurgeEvents()
    {
        return "DELETE FROM Event WHERE Timestamp<?";
    }

    public String getPurgeICMP()
    {
        return "DELETE FROM icmphdr WHERE ( sid, cid ) IN ( SELECT sid, cid FROM Event WHERE Timestamp<? )";
    }

    public String getPurgeIP()
    {
        return "DELETE FROM iphdr WHERE ( sid, cid ) IN ( SELECT sid, cid FROM Event WHERE Timestamp<? )";
    }

    public String getPurgeTCP()
    {
        return "DELETE FROM tcphdr WHERE ( sid, cid ) IN ( SELECT sid, cid FROM Event WHERE Timestamp<? )";
    }

    public String getPurgeUDP()
    {
        return "DELETE FROM udphdr WHERE ( sid, cid ) IN ( SELECT sid, cid FROM Event WHERE Timestamp<? )";
    }

    public String getPurgeData()
    {
        return "DELETE FROM Data WHERE ( sid, cid ) IN ( SELECT sid, cid FROM Event WHERE Timestamp<? )";
    }

    public String getPurgeOpt()
    {
        return "DELETE FROM Opt WHERE ( sid, cid ) IN ( SELECT Sid, cid FROM Event WHERE Timestamp<? )";
    }

    public String getPurgeSignature()
    {
        return "DELETE FROM Signature WHERE ( sid, cid ) IN ( SELECT Sid, cid FROM Event WHERE Timestamp<? )";
    }

    public String getData(long sid, long cid)
    {
        return "SELECT * FROM Data WHERE cid=" + cid + " AND sid=" + sid;
    }

    public String getSignature(long sid, long cid)
    {
        return "SELECT S.* FROM Signature S, Event E WHERE S.sig_id=E.signature AND "
                + "E.cid=" + cid + " AND E.sid=" + sid;
    }
    
    public String getAlertsBySeverity()
    {
        return "SELECT sig_priority, count(sig_priority) as total FROM event "
        + "LEFT JOIN signature ON event.signature = signature.sig_id "
        + "WHERE ( EXTRACT( EPOCH FROM (Now() - Timestamp) ) ) <= 60*5 "
        + "GROUP BY sig_priority";
    }
    
    public String getAlertsByProtocol()
    {
        return "SELECT ip_proto, count(ip_proto) as total "
        + "FROM event LEFT JOIN iphdr ON event.sid = iphdr.sid and event.cid = iphdr.cid "
        + "WHERE ( EXTRACT( EPOCH FROM (Now() - Timestamp) ) ) <= 60*5 "
        + "GROUP BY ip_proto";
    }
}