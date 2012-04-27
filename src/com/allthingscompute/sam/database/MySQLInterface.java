package com.allthingscompute.sam.database;

/**
 * This class is a SQL database Interface to MySQL. It defines the strings to be
 * used to do various SAM related selects for a MySQL snort database.
 * 
 * @author E. Internicola <BR>
 *         Date 05/05/2003
 * @see DatabaseInterface
 */
public class MySQLInterface implements DatabaseInterface
{

    public String getNumOfAttacks()
    {
        return "SELECT COUNT(*) FROM event WHERE timestamp > (DATE_SUB(NOW(), INTERVAL ? MINUTE))";
    }

    public String getNumOfAttacksForever()
    {
        return "SELECT COUNT(*) FROM event";
    }

    public String getCurDatabaseTime()
    {
        return "SELECT NOW()";
    }

    public String getTopAttackers()
    {
        return "SELECT DISTINCT inet_ntoa(iphdr.ip_src) as ip_addr, COUNT(event.cid) AS num_events FROM event "
                + "LEFT JOIN iphdr ON ((event.sid=iphdr.sid) AND (event.cid=iphdr.cid)) WHERE (ip_src > -1 AND event.timestamp > "
                + "(DATE_SUB(NOW(), INTERVAL 24 HOUR))) GROUP BY ip_src ORDER BY num_events DESC LIMIT 0,15";
    }

    public String getTopAttacks()
    {
        return "SELECT sig_name, count(signature) as sig_cnt FROM event LEFT JOIN signature ON "
                + "event.signature=signature.sig_id WHERE event.cid > 0 AND event.timestamp > (DATE_SUB(NOW(), INTERVAL 24 HOUR)) "
                + "GROUP BY signature ORDER BY sig_cnt DESC LIMIT 0, 15";
    }

    public String getAttacksLastHour()
    {
        return "SELECT S.*, SE.*, E.*, I.* FROM sensor SE, signature S, "
                + "event E LEFT JOIN iphdr I ON E.sid=I.sid AND E.cid=I.cid "
                + "WHERE E.cid > 0 AND SE.sid=E.sid "
                + "AND E.timestamp > ( DATE_SUB( NOW(), INTERVAL 1 HOUR) ) "
                + "AND E.signature=S.sig_id ORDER BY E.timestamp DESC";
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
        return "SELECT S.*, SE.*, E.*, I.* FROM sensor SE, signature S, event E LEFT JOIN iphdr I ON "
                + "E.sid=I.sid AND E.cid=I.cid WHERE E.cid > 0 "
                + "AND E.Timestamp > ( DATE_SUB( NOW(), INTERVAL "
                + hours
                + " HOUR) ) "
                + "AND S.sig_name='"
                + attackType.replaceAll("'", "''")
                + "' AND SE.sid=E.sid "
                + "AND E.signature=S.sig_id ORDER BY timestamp DESC";
    }

    public String getRelatedSources(long source, long hours)
    {
        return "SELECT S.*, SE.*, E.*, I.* FROM sensor SE, signature S, event E LEFT JOIN iphdr I ON "
                + "E.sid=I.sid AND E.cid=I.cid WHERE E.cid > 0 "
                + "AND E.timestamp > ( DATE_SUB( NOW(), INTERVAL "
                + hours
                + " HOUR) ) "
                + "AND I.ip_src="
                + source
                + " AND SE.sid=E.sid "
                + "AND E.signature=S.sig_id ORDER BY timestamp DESC";
    }

    public String getRelatedDestinations(long dest, long hours)
    {
        return "SELECT S.*, SE.*, E.*, I.* FROM sensor SE, signature S, event E LEFT JOIN iphdr I ON "
                + "E.sid=I.sid AND E.cid=I.cid WHERE E.cid > 0 AND SE.sid=E.sid "
                + "AND E.timestamp > ( DATE_SUB( NOW(), INTERVAL "
                + hours
                + " HOUR) ) "
                + "AND I.ip_dst="
                + dest
                + " "
                + "AND E.signature=S.sig_id ORDER BY timestamp DESC";
    }

    public String getPurgeEvents()
    {
        return "DELETE FROM event WHERE timestamp<?";
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
        return "DELETE FROM data WHERE ( sid, cid ) IN ( SELECT sid, cid FROM Event WHERE Timestamp<? )";
    }

    public String getPurgeOpt()
    {
        return "DELETE FROM opt WHERE ( sid, cid ) IN ( SELECT sid, cid FROM Event WHERE Timestamp<? )";
    }

    public String getPurgeSignature()
    {
        return "DELETE FROM signature WHERE ( sid, cid ) IN ( SELECT sid, cid FROM Event WHERE Timestamp<? )";
    }

    public String getData(long sid, long cid)
    {
        return "SELECT * FROM data WHERE cid=" + cid + " AND sid=" + sid;
    }

    public String getSignature(long sid, long cid)
    {
        return "SELECT S.* FROM signature S, event E WHERE S.sig_id=E.signature AND "
                + "E.cid=" + cid + " AND E.sid=" + sid;
    }
    
    public String getAlertsBySeverity()
    {
        return "SELECT sig_priority, count(sig_priority) as total FROM event "
        + "LEFT JOIN signature ON event.signature = signature.sig_id "
        + "WHERE timestamp > (DATE_SUB(NOW(), INTERVAL 5 MINUTE)) "
        + "GROUP BY sig_priority";
    }
    
    public String getAlertsByProtocol()
    {
        return "SELECT ip_proto, count(ip_proto) as total "
        + "FROM event LEFT JOIN iphdr ON event.sid = iphdr.sid and event.cid = iphdr.cid "
        + "WHERE timestamp > (DATE_SUB(NOW(), INTERVAL 5 MINUTE)) "
        + "GROUP BY ip_proto";
    }
}