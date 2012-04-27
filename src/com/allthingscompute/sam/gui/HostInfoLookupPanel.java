package com.allthingscompute.sam.gui;

import java.net.InetAddress;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * This class currently is unimplemented.
 * 
 * @author sfreiberg, Documented by E. Internicola <br>
 *         Date Aug 21, 2002
 */
public class HostInfoLookupPanel extends JPanel
{
    protected JComboBox whoisServers;

    protected InetAddress hostAddress;

    protected JTextField hostAddressTextField;

    protected JTextArea whoisResultsTextArea;

    protected JButton lookupButton, closeButton;

    public HostInfoLookupPanel(InetAddress hostAddress)
    {

    }

    public HostInfoLookupPanel()
    {
        this(null);
    }
}

