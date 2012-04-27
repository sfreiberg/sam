
package com.allthingscompute.sam.gui.Dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.allthingscompute.sam.Common;
import com.allthingscompute.sam.config.PropertiesManager;
import com.allthingscompute.sam.database.DatabaseConnection;
import com.allthingscompute.sam.database.DatabaseInterface;
import com.allthingscompute.sam.log.Logger;

/**
 * This class currently is unimplemented.
 * 
 * @author sfreiberg, Documented by E. Internicola <br>
 *         Date Aug 21, 2002
 */
public class DataPurge extends JFrame implements ActionListener
{
    private JLabel[] jlText = null;

    private JButton jbOK = null;

    private JButton jbCancel = null;

    private JTextField jtfDate = null;

    private Container[] ctns = null;

    private JComboBox jcbChoices = null;

    private Vector vCombo = new Vector();

    private JTextArea jtInfo;

    /**
     * Constructor - creates the window.
     */
    public DataPurge()
    {
        super("Purge Data");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        jlText = new JLabel[3];
        ctns = new Container[2];
        jlText[0] = new JLabel(
                "This will Purge all records that occur before the provided date.");
        jlText[1] = new JLabel("Date");
        jlText[2] = new JLabel("Criteria");
        jtInfo = new JTextArea(jlText[0].getText());
        jtInfo.setEditable(false);
        jtInfo.setBackground(Color.LIGHT_GRAY);

        vCombo.add("On");
        vCombo.add("Before");

        ctns[0] = new Container();
        ctns[1] = new Container();
        jtfDate = new JTextField(Common.getFormattedDate());
        jbOK = new JButton("Purge");
        jbCancel = new JButton("Cancel");
        jcbChoices = new JComboBox(vCombo);
        jbOK.addActionListener(this);
        jbCancel.addActionListener(this);

        ctns[0].setLayout(new GridLayout(1, 2));
        ctns[1].setLayout(new GridLayout(1, 2));
        getContentPane().add(jtInfo, BorderLayout.CENTER);
        getContentPane().add(ctns[0], BorderLayout.NORTH);
        getContentPane().add(ctns[1], BorderLayout.SOUTH);
        ctns[0].add(jlText[1]);
        ctns[0].add(jtfDate);
        ctns[1].add(jbOK);
        ctns[1].add(jbCancel);

        setVisible(true);
    }

    /**
     * Inherited Method - handles the button click events.
     */
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource().equals(jbOK))
        {
            jbOK.setEnabled(false);
            jbCancel.setEnabled(false);
            Purger p = new Purger(jbOK, jbCancel, jtfDate.getText());
            p.start();
        } else if (e.getSource().equals(jbCancel))
        {
            setVisible(false);
            dispose();
        }
    }
}

class Purger extends Thread
{
    private JButton jbOK = null;

    private JButton jbCancel = null;

    private String strDate = null;

    private Progress pg = null;

    public Purger(JButton jButtonOK, JButton jButtonCancel, String date)
    {
        jbOK = jButtonOK;
        jbCancel = jButtonCancel;
        strDate = date;
        pg = new Progress(0, 8);

    }

    public void run()
    {
        //System.out.println( jtfDate.getText() );
        try
        {
            DatabaseInterface di = PropertiesManager.getDatabaseInterface();
            Connection c = DatabaseConnection.getDatabaseConnection()
                    .getConnection();
            PreparedStatement ps = null;

            pg.setStatus(0, "Purging ICMP Records");
            //  1
            //purge ICMP records
            ps = c.prepareStatement(di.getPurgeICMP());
            ps.setString(1, strDate);
            ps.execute();
            pg.setStatus(1, "Purging IP Records");
            //  2
            //purge IP records
            ps = c.prepareStatement(di.getPurgeIP());
            ps.setString(1, strDate);
            ps.execute();

            pg.setStatus(2, "Pugring TCP Records");
            //  3
            //purge TCP records
            ps = c.prepareStatement(di.getPurgeTCP());
            ps.setString(1, strDate);
            ps.execute();

            pg.setStatus(3, "Purging UDP Records");
            //  4
            //purge UDP records
            ps = c.prepareStatement(di.getPurgeUDP());
            ps.setString(1, strDate);
            ps.execute();

            pg.setStatus(4, "Purging Data Records");
            //  5
            //purge Data records
            ps = c.prepareStatement(di.getPurgeData());
            ps.setString(1, strDate);
            ps.execute();

            pg.setStatus(5, "Purging Opt Records");
            //  6
            //purge Opt records
            ps = c.prepareStatement(di.getPurgeOpt());
            ps.setString(1, strDate);
            ps.execute();

            pg.setStatus(6, "Purging Signature Records");
            //  7
            //purge Signature records
            ps = c.prepareStatement(di.getPurgeSignature());
            ps.setString(1, strDate);
            ps.execute();

            pg.setStatus(7, "Purging Events");
            //  8
            //purge Events
            ps = c.prepareStatement(di.getPurgeEvents());
            ps.setString(1, strDate);
            ps.execute();
            pg.setStatus(8, "Finished");
            pg.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        } catch (SQLException sqle)
        {
            sqle.printStackTrace();
            System.out.println("SQL Exception:\t" + sqle);
            Logger.writeToLog(sqle.getMessage());
        } catch (Exception ex)
        {
            ex.printStackTrace();
            System.out.println("Exception:\t" + ex);
            Logger.writeToLog(ex.getMessage());
        }
        jbOK.setEnabled(true);
        jbCancel.setEnabled(true);
    }
}

class Progress extends JFrame
{
    private JProgressBar progressBar = null;

    private JLabel jlbl = null;

    public Progress(int min, int max)
    {
        super("Progress");
        setSize(500, 200);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        getContentPane().setLayout(new FlowLayout());
        progressBar = new JProgressBar(min, max);
        getContentPane().add(progressBar);
        progressBar.setValue(min);
        progressBar.setStringPainted(true);
        pack();
        setVisible(true);
    }

    public void setStatus(int amt, String text)
    {
        //progressBar.setStringPainted( true );
        progressBar.setValue(amt);
        progressBar.setString(text);
    }
}