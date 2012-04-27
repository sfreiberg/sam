package com.allthingscompute.sam.alert;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.allthingscompute.sam.config.PropertiesManager;
import com.allthingscompute.sam.log.Logger;

/**
 * This class is used to send an email alert to an array of recipients (network
 * admins perhaps?). When the alert changes, an email will be fired out to
 * whomever is in the recipient list.
 * 
 * @author Sam, Documented by E. Internicola
 */
public class EmailAlert implements AlertLevelChangeListener,
        PropertyChangeListener
{
    //	ArrayList emailAddresses;

    private boolean sendEmail;

    private String host;

    private String from;

    private ArrayList to = new ArrayList();

    private String msgContent = "This is a message to warn you that we "
            + "have reached a 'red' status. SAM Development edition";

    /** */
    private Properties sysProps = System.getProperties();

    /**
     *  
     */
    public EmailAlert() throws Exception
    {
        PropertiesManager propsManager = PropertiesManager
                .getPropertiesManager();
        host = propsManager.getValue("email.host");
        from = propsManager.getValue("email.from");
        if (propsManager.getValue("email.active").toString().equalsIgnoreCase(
                "true"))
        {
            sendEmail = true;
        } else
        {
            sendEmail = false;
        }
        StringTokenizer tokenizer = new StringTokenizer(propsManager
                .getValue("email.to"), ",");
        while (tokenizer.hasMoreTokens())
        {
            to.add(tokenizer.nextToken());
        }
        sysProps.put("mail.smtp.host", host);
    }

    /**
     *  
     */
    public void alertLevelChanged(int alertLevel)
    {
        if (alertLevel == AlertLevel.HIGH_ALERT_LEVEL && sendEmail)
        {
            Session session = Session.getDefaultInstance(sysProps, null);
            try
            {
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from));
                for (int i = 0; i < to.size(); i++)
                {
                    message.addRecipient(Message.RecipientType.TO,
                            new InternetAddress(to.get(i).toString()));
                }
                message.setSubject("Snort Alert");
                message.setContent(msgContent, "text/plain");

                // Send message
                Transport.send(message);
            } catch (Exception e)
            {
                Logger.writeToLog("Error sending mail");
                //System.out.println("Error sending mail");
            }
        }
    }

    /**
     *  
     */
    public String toString()
    {
        return "Email Alert";
    }

    /**
     *  
     */
    public void propertyChange(PropertyChangeEvent event)
    {
        if (event.getPropertyName().equalsIgnoreCase("email.active"))
        {
            String newValue = event.getNewValue().toString();
            if (newValue.equalsIgnoreCase("true"))
            {
                sendEmail = true;
            } else
            {
                sendEmail = false;
            }
        }
    }
}