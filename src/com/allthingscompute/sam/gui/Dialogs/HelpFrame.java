/*
 * HelpFrame.java
 *
 * Created on March 29, 2004, 10:42 AM
 */

package com.allthingscompute.sam.gui.Dialogs;

import java.awt.Dimension;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;
import javax.swing.text.html.StyleSheet;

/**
 * 
 * @author E. Internicola
 */
public class HelpFrame extends JFrame implements HyperlinkListener
{
    private JEditorPane jep = null;

    private HTMLEditorKit myEditorKit = null;

    private StyleSheet myStyleSheet = null;

    public HelpFrame(String url) throws Exception
    {
        super("External Page");
        initialize(url);
    }

    public HelpFrame() throws Exception
    {
        super("Help");
        initialize("file:help/index.html");
    }

    private void initialize(String url) throws Exception
    {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        myEditorKit = new HTMLEditorKit();
        jep = new JEditorPane();
        jep.setEditorKit(myEditorKit);
        jep.setContentType("text/html");
        jep.setPage(url);
        jep.setEditable(false);
        jep.addHyperlinkListener(this);
        this.getContentPane().add(jep);
        setSize(new Dimension(800, 600));
        setVisible(true);
    }

    /**
     *  
     */
    public void hyperlinkUpdate(HyperlinkEvent e)
    {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
        {
            JEditorPane pane = (JEditorPane) e.getSource();
            if (e instanceof HTMLFrameHyperlinkEvent)
            {
                HTMLFrameHyperlinkEvent evt = (HTMLFrameHyperlinkEvent) e;
                HTMLDocument doc = (HTMLDocument) pane.getDocument();
                if (evt.getTarget().equals("_new"))
                {
                    try
                    {
                        new HelpFrame(evt.getURL().toString());
                    } catch (Exception ex)
                    {
                        ex.printStackTrace();
                        System.out
                                .println("Exception openning new Browser Window:\t"
                                        + e);
                    }
                }
                doc.processHTMLFrameHyperlinkEvent(evt);
            } else
            {
                try
                {
                    pane.setPage(e.getURL());
                } catch (Throwable t)
                {
                    t.getMessage();
                    //t.printStackTrace();
                }
            }
        }
    }

}