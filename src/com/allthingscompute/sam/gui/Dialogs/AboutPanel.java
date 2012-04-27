package com.allthingscompute.sam.gui.Dialogs;

import java.awt.BorderLayout;
import java.io.FileReader;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import com.allthingscompute.sam.log.Logger;

/**
 * This class is just used to display the "About" information for SAM.
 * 
 * @author sfreiberg, Documented by E. Internicola <BR>
 *         Date Jul 9, 2002
 */
public class AboutPanel extends JDialog
{
    /** The Title of the window. */
    private static String TITLE = "About S.A.M.";

    /**
     * Constructor that allows the Dialog to be owned by a JFrame object, sets
     * the title, and sets the dialog as Modal.
     */
    public AboutPanel(JFrame frame)
    {
        super(frame, TITLE, true);
        init();
    }

    /**
     * Constructor that allows the Dialog to be owned by a JDialog object, sets
     * the title, and sets the dialog as modal.
     */
    public AboutPanel(JDialog dialog)
    {
        super(dialog, TITLE, true);
        init();
    }

    /**
     * This method handles setting up the objects within the frame.
     */
    private void init()
    {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        //		setLocationRelativeTo(parent);

        // Create Components
        JTextArea licenseTextArea = new JTextArea();
        licenseTextArea.setWrapStyleWord(true);
        licenseTextArea.setRows(10);
        licenseTextArea.setColumns(40);
        try
        {
            licenseTextArea.read(new FileReader("lic/LAFGPL-SAM.txt"), null);
        } catch (Exception e)
        {
            Logger.writeToLog(e.getMessage());
        }

        JTextArea creditsTextArea = new JTextArea();
        creditsTextArea.setWrapStyleWord(true);
        creditsTextArea.setRows(10);
        creditsTextArea.setColumns(40);
        try
        {
            creditsTextArea.read(new FileReader("lic/credits.txt"), null);
        } catch (Exception e)
        {
            Logger.writeToLog(e.getMessage());
        }

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Credits", new JScrollPane(creditsTextArea,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
        tabbedPane.addTab("License", new JScrollPane(licenseTextArea,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));

        // Add components
        getContentPane().setLayout(new BorderLayout(10, 10));
        //		getContentPane().add(
        //			new JLabel(new ImageIcon("icons/stoplight_green.gif")),
        //			BorderLayout.WEST);
        getContentPane().add(
                new JLabel("Snort Alert Monitor", SwingConstants.CENTER),
                BorderLayout.NORTH);
        getContentPane().add(tabbedPane, BorderLayout.CENTER);

        // Set defaults for About box and display
        pack();
        setVisible(true);
    }
}