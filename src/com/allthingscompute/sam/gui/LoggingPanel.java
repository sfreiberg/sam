package com.allthingscompute.sam.gui;

import javax.swing.JTextArea;

import com.allthingscompute.sam.log.Loggable;

/**
 * This class is just a uneditable textbox used to display log data. <BR>
 * <BR>
 * Sample Depiction: <BR>
 * <IMG SRC="../LoggingPanel.png" BORDER="0">
 * 
 * @author Sam, Documented by E. Internicola
 */
public class LoggingPanel extends JTextArea implements Loggable
{
    /**
     * Constructor - sets the editable property to false.
     */
    LoggingPanel()
    {
        super();
        setEditable(false);
    }

    /**
     * Wrapper function for append. It does nothing different, it is makes it
     * more obvious what is going on.
     */
    public void writeToLog(String s)
    {
        append(s);
    }

    /**
     * Clears the entire text area.
     */
    public void clearLog()
    {
        setText("");
    }
}