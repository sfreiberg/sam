package com.allthingscompute.sam.gui.Dialogs;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.allthingscompute.sam.alert.AlertInfo;

/**
 * This class shows details about a specific alert.
 * 
 * @author sfreiberg, Documented by E. Internicola <BR>
 *         Date Aug 21, 2002
 */
public class AlertDetailPanel extends JPanel
{
    /** The Font type. */
    private Font normalFont = new Font("Dialog", 0, 12);

    /**
     * The constructor that takes an alertInfo object as a parameter. It then
     * creates 2 panels, one with information regarding the source, the other
     * with information regarding the destination.
     */
    public AlertDetailPanel(AlertInfo alertInfo)
    {
        add(createHostPanel("Source", alertInfo.getSourceAddress()
                .getHostName(), alertInfo.getSourceAddress().getHostAddress(),
                alertInfo.getSourcePort()));
        add(createHostPanel("Destination", alertInfo.getDestinationAddress()
                .getHostName(), alertInfo.getDestinationAddress()
                .getHostAddress(), alertInfo.getDestinationPort()));
    }

    /**
     * Private method for building the actual panel itself.
     */
    private JPanel createHostPanel(String title, String domainName,
            String ipAddress, String port)
    {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder(title));

        JLabel domainNameLabel = new JLabel(domainName);
        domainNameLabel.setFont(normalFont);
        JLabel ipAddressLabel = new JLabel(ipAddress);
        ipAddressLabel.setFont(normalFont);
        JLabel portLabel = new JLabel(port);
        portLabel.setFont(normalFont);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(2, 5, 5, 5);
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0;
        c.gridy = 0;
        panel.add(new JLabel("Domain Name"), c);
        c.gridy = GridBagConstraints.RELATIVE;
        panel.add(new JLabel("IP Address"), c);
        panel.add(new JLabel("Port"), c);
        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(domainNameLabel, c);
        panel.add(ipAddressLabel, c);
        panel.add(portLabel, c);

        return panel;
    }

    public void actionPerformed(java.awt.event.ActionEvent e)
    {
    }

}