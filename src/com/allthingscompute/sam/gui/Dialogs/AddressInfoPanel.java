package com.allthingscompute.sam.gui.Dialogs;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.net.InetAddress;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 * Shows the IP address and Hostename if available. I'm not sure exactly what
 * creates one of these, I will have to cross reference it though....
 * 
 * @author sfreiberg, Documented by E. Internicola <BR>
 *         Date Aug 21, 2002
 *  
 */
public class AddressInfoPanel extends JPanel
{
    Font normalFont = new Font("Dialog", 0, 12);

    /**
     *  
     */
    public AddressInfoPanel(InetAddress address)
    {
        add(createHostPanel("Host", address.getHostName(), address
                .getHostAddress()));
    }

    /**
     *  
     */
    public AddressInfoPanel(String domainName, String ipAddress)
    {
        add(createHostPanel("Host", domainName, ipAddress));
    }

    /**
     *  
     */
    JPanel createHostPanel(String title, String domainName, String ipAddress)
    {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder(title));

        JLabel domainNameLabel = new JLabel(domainName);
        domainNameLabel.setFont(normalFont);
        JLabel ipAddressLabel = new JLabel(ipAddress);
        ipAddressLabel.setFont(normalFont);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(2, 5, 5, 5);
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0;
        c.gridy = 0;
        panel.add(new JLabel("Domain Name"), c);
        c.gridy = GridBagConstraints.RELATIVE;
        panel.add(new JLabel("IP Address"), c);
        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(domainNameLabel, c);
        panel.add(ipAddressLabel, c);
        return panel;
    }
}