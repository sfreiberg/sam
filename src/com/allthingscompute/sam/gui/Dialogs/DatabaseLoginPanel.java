package com.allthingscompute.sam.gui.Dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.FileFilter;
import java.util.Date;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.allthingscompute.sam.config.PropertiesManager;
import com.allthingscompute.sam.database.DatabaseConfigFile;
import com.allthingscompute.sam.database.DatabaseConnection;
import com.allthingscompute.sam.log.Logger;

/**
 * This class is a dialog that manages a connection to a SAM database. When it
 * loads it has some background things that it does. Some of these tasks
 * include:
 * <OL>
 * <LI>Create a File Filter for files prefixed with "db." and suffixed with
 * ".properties"</LI>
 * <LI>Index each of these types of files (in ./conf/db).</LI>
 * <LI>Populate a vector with Database types from the above described files.
 * </LI>
 * <LI>Display the data on the form.</LI>
 * </OL>
 * <BR>
 * <BR>
 * Sample Depiction: <BR>
 * <IMG SRC="../DatabaseLoginPanel.jpg">
 * 
 * @version 1.0
 * @author Modified and Documented by E. Internicola <BR>
 *         3/29/2003
 * @see com.allthingscompute.sam.database.DatabaseConfigFile
 * @see com.allthingscompute.sam.database.DatabaseConnection
 */
public class DatabaseLoginPanel extends JDialog implements ActionListener,
        ComponentListener
{
    private JLabel databaseLabel = new JLabel("Database", SwingConstants.CENTER);

    private JLabel databaseNameLabel = new JLabel("Database Name",
            SwingConstants.CENTER);

    private JLabel hostnameLabel = new JLabel("Hostname", SwingConstants.CENTER);

    private JLabel usernameLabel = new JLabel("Username", SwingConstants.CENTER);

    private JLabel passwordLabel = new JLabel("Password", SwingConstants.CENTER);

    private JLabel databaseTypeLabel = new JLabel("Database Type",
            SwingConstants.CENTER);

    private JTextField databaseNameField = null;

    private JTextField hostnameField = null;

    private JTextField usernameField = null;

    private JPasswordField passwordField = null;

    private JButton okButton = null;

    private JButton cancelButton = null;

    private JComboBox databaseUID = null;

    private JComboBox databaseType = null;

    private DatabaseConfigFile dcf = null;

    /** Vector of database types. */
    private Vector databaseVector = null;

    private Vector databaseTypeVector = null;

    /** Maximum login tries, once exceeded; the program exits. */
    private int maxLoginTrys = 3;

    /** Number of tries. */
    private int trys = 0;

    /**
     * Constructor, takes a (owner) frame as a parameter. This creates the
     * dialog, then adds all of the elements to it.
     */
    private DatabaseLoginPanel(JFrame frame)
    {
        super(frame, "Database Login", true);
        init();
    }

    /**
     * Intialize the Frame elements, and add them to their respective positions.
     */
    private void init()
    {
        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints gridConstraints = new GridBagConstraints();

        // Create Vector of available database types
        databaseVector = new Vector();
        databaseTypeVector = new Vector();
        databaseTypeVector.add("PostgreSQL");
        databaseTypeVector.add("MySQL");

        //create an array of "Database Configuration Files"; by reading them
        // from the conf/db folder.
        File[] databaseConfigFiles = new File("conf/db")
                .listFiles(new FileFilter()
                {
                    public boolean accept(File file)
                    {
                        String fileName = file.getName();
                        return (!file.isDirectory());
                    }
                });
        for (int i = 0; i < databaseConfigFiles.length; i++)
        {
            DatabaseConfigFile confFile = new DatabaseConfigFile(
                    databaseConfigFiles[i]);
            databaseVector.add(confFile);
        }

        databaseUID = new JComboBox(databaseVector);
        databaseType = new JComboBox(databaseTypeVector);
        databaseUID.addActionListener(this);

        databaseNameField = new JTextField(10);
        hostnameField = new JTextField(10);
        usernameField = new JTextField(10);
        passwordField = new JPasswordField(10);

        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");

        okButton.addActionListener(this);
        cancelButton.addActionListener(this);

        // Add Components to panel
        gridConstraints.insets = new Insets(3, 3, 3, 3);
        gridConstraints.gridy = 0;
        gridConstraints.gridx = 0;
        gridConstraints.anchor = GridBagConstraints.EAST;
        getContentPane().add(databaseLabel, gridConstraints);
        gridConstraints.gridy = GridBagConstraints.RELATIVE;
        getContentPane().add(databaseTypeLabel, gridConstraints);
        getContentPane().add(hostnameLabel, gridConstraints);
        getContentPane().add(databaseNameLabel, gridConstraints);
        getContentPane().add(usernameLabel, gridConstraints);
        getContentPane().add(passwordLabel, gridConstraints);

        gridConstraints.gridx = 1;
        gridConstraints.anchor = GridBagConstraints.WEST;
        getContentPane().add(databaseUID, gridConstraints);
        getContentPane().add(databaseType, gridConstraints);
        getContentPane().add(hostnameField, gridConstraints);
        getContentPane().add(databaseNameField, gridConstraints);
        getContentPane().add(usernameField, gridConstraints);
        getContentPane().add(passwordField, gridConstraints);

        JPanel buttonPnl = new JPanel(new GridLayout(1, 2));
        buttonPnl.add(okButton);
        buttonPnl.add(cancelButton);
        gridConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridConstraints.gridx = 0;
        gridConstraints.gridy = GridBagConstraints.RELATIVE;
        gridConstraints.anchor = GridBagConstraints.CENTER;
        getContentPane().add(buttonPnl, gridConstraints);

        getRootPane().setDefaultButton(okButton);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loadDatabaseInfo();
        loadInitialConfig();
        addComponentListener(this);
        setSize(800, 600);
        pack();
    }

    /**
     * This method is inherited from the ComponentListener interface.
     */
    public void componentHidden(ComponentEvent e)
    {
    }

    /**
     * This method is inherited from the ComponentListener interface.
     */
    public void componentMoved(ComponentEvent e)
    {
    }

    /**
     * This method is inherited from the ComponentListener interface.
     */
    public void componentShown(ComponentEvent e)
    {
    }

    /**
     * This method is inherited from the ComponentListener interface. When the
     * window is resized, this will resize the components to fit the screen
     * well.
     */
    public void componentResized(ComponentEvent e)
    {
        resizeComponents();
    }

    private void resizeComponents()
    {
        databaseNameField.setSize((getWidth() / 2) - 10, databaseNameField
                .getHeight());
        hostnameField.setSize((getWidth() / 2) - 10, hostnameField.getHeight());
        usernameField.setSize((getWidth() / 2) - 10, usernameField.getHeight());
        passwordField.setSize((getWidth() / 2) - 10, passwordField.getHeight());
        databaseUID.setSize((getWidth() / 2) - 10, databaseUID.getHeight());
    }

    /**
     * This method creates a DatabaseLoginPanel, initializes it, and then
     * returns a reference to it.
     */
    public static DatabaseLoginPanel getDatabaseLoginPanel(JFrame frame)
    {
        DatabaseLoginPanel loginPanel = new DatabaseLoginPanel(frame);
        loginPanel.pack();
        loginPanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return loginPanel;
    }

    /**
     * Event handler method, listens for various user events.
     */
    public void actionPerformed(ActionEvent event)
    {
        Object o = event.getSource();
        if (o.equals(okButton))
        {
            DatabaseConfigFile dcf = (DatabaseConfigFile) databaseUID
                    .getSelectedItem();
            try
            {
                saveDatabaseInfo();
                /*
                 * DatabaseConnection dbConn = new DatabaseConnection(
                 * getHostName(), getDatabaseName(), getUsername(),
                 * getPassword(), ( DatabaseConfigFile )
                 * databaseUID.getSelectedItem() );
                 */
                DatabaseConnection dbConn = new DatabaseConnection(
                        dcf.getURL(), dcf.getUsername(), dcf.getPassword(), dcf
                                .getDriver());
                if (dbConn.isConnected())
                {
                    DatabaseConnection.setDatabaseConnection(dbConn);
                    PropertiesManager.getPropertiesManager().setValue(
                            "DatabaseUID", dcf.getUID());
                    PropertiesManager.getPropertiesManager().setValue(
                            "DatabaseType", dcf.getDatabaseType());
                    //only save the database information after the connection
                    // has been established
                    dispose();
                    return;
                }
            } catch (Exception exception)
            {
                Logger.writeToLog(new Date()
                        + "\tFailed to login to database:\t" + exception);
                JOptionPane.showMessageDialog(this, exception.getMessage());
            }
            // Increment the number of trys and check the total number of trys
            trys++;
            if (trys == maxLoginTrys)
            {
                JOptionPane.showMessageDialog(this, "Too many login attempts!");
                System.exit(0);
            }
        } else if (o.equals(cancelButton))
        {
            System.exit(0);
        }

        /**
         * If the user changes the database in the "Database" combobox, then
         * display the data that was read from the text file.
         */
        else if (o.equals(databaseUID))
        {
            //if the user selects a different database, then load the data
            // dynamically.
            if (event.getActionCommand().compareTo("comboBoxChanged") == 0)
            {
                loadDatabaseInfo();
            }
        }
    }

    private String getDatabaseType()
    {
        int i = databaseType.getSelectedIndex();
        return (String) databaseTypeVector.elementAt(i);
    }

    private String getHostName()
    {
        String[] parts = hostnameField.getText().split(":");
        return parts[0];
    }

    private String getPort()
    {
        String[] parts = hostnameField.getText().split(":");
        return parts[1];
    }

    private String getDatabaseName()
    {
        return databaseNameField.getText();
    }

    private String getUsername()
    {
        return usernameField.getText();
    }

    private String getPassword()
    {
        return new String(passwordField.getPassword());
    }

    private void saveDatabaseInfo()
    {
        dcf = (DatabaseConfigFile) databaseUID.getSelectedItem();
        dcf.setDatabaseType(getDatabaseType());
        dcf.setHostName(getHostName());
        dcf.setPort(getPort());
        dcf.setDatabaseName(getDatabaseName());
        dcf.setUsername(getUsername());
        dcf.setPassword(getPassword());
        dcf.writeFile();
    }

    private void loadInitialConfig()
    {
        DatabaseConfigFile dcf = null;
        for (int i = 0; i < databaseUID.getItemCount(); i++)
        {
            dcf = (DatabaseConfigFile) databaseUID.getItemAt(i);
            try
            {
                String dataSource = PropertiesManager.getPropertiesManager()
                        .getValue("DatabaseUID");
                if (dcf.getUID().equals(dataSource))
                {
                    databaseUID.setSelectedIndex(i);
                    return;
                }
            } catch (Exception e)
            {
                e.printStackTrace();
                System.out.println("Exception loading initial database info:\t"
                        + e);
            }
        }
    }

    private void setDatabaseType(String dbtype)
    {
        for (int i = 0; i < databaseTypeVector.size(); i++)
        {
            String dte = (String) databaseTypeVector.elementAt(i);
            if (dte.toLowerCase().equals(dbtype.toLowerCase()))
            {
                databaseType.setSelectedIndex(i);
                return;
            }
        }
    }

    /**
     * This method will load the data (that was read from the database file) to
     * the form element to be displayed (and/or edited).
     */
    private void loadDatabaseInfo()
    {
        DatabaseConfigFile dcf = (DatabaseConfigFile) databaseUID
                .getSelectedItem();
        setDatabaseType(dcf.getDatabaseType());
        hostnameField.setText(dcf.getHostName() + ":" + dcf.getPort());
        databaseNameField.setText(dcf.getDatabaseName());
        usernameField.setText(dcf.getUsername());
        passwordField.setText(dcf.getPassword());
    }
}

