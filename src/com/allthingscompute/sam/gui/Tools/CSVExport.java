/*
 * CSVExport.java
 *
 * Created on March 15, 2004, 10:05 AM
 */

package com.allthingscompute.sam.gui.Tools;

import java.awt.Component;
import java.io.File;
import java.io.FileOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JTable;

/**
 * This class will export a JTable or an AttackVector to CSV Format.
 * 
 * @author E. Internicola
 */
public class CSVExport
{
    private File output = null;

    private JTable jtData = null;

    //private AttackVector av = null;
    private String exportData = null;

    private JFileChooser jfc = null;

    private Component parent;

    public CSVExport()
    {
    }

    public CSVExport(JTable jtData)
    {
        setJTable(jtData);
    }

    //public CSVExport( AttackVector av ) { setAttackVector( av ); }

    public void promptFile()
    {
        jfc = new JFileChooser();
        int action = jfc.showDialog(parent, "Save");
        if (action == JFileChooser.APPROVE_OPTION)
        {
            setFile(jfc.getSelectedFile());
            save();
        }
    }

    public void setFile(File output)
    {
        this.output = output;
    }

    public void setFile(String outputFileName)
    {
        setFile(new File(outputFileName));
    }

    /*
     * public void setAttackVector( AttackVector av ) { this.av = av; }
     */

    public void setJTable(JTable jtData)
    {
        this.jtData = jtData;
        this.exportData = getJTableData();
    }

    /**
     * This method will prepare text for it's place in the CSV file (wrapping it
     * in quotes if necessary).
     */
    private String getCSVField(String text)
    {
        if (text.matches(","))
        {
            return "\'" + text.replaceAll("'", "''") + "\'";
        }
        return text;
    }

    /**
     * Create a CSV Export String from the JTable.
     */
    private String getJTableData()
    {
        String jtd = new String();
        for (int i = 0; i < jtData.getColumnCount(); i++)
        {
            String text = jtData.getColumnName(i);
            if (i != 0)
            {
                jtd += ",";
            }
            jtd += getCSVField(text);
        }
        jtd += "\n";
        int rows = jtData.getModel().getRowCount();
        int cols = jtData.getModel().getColumnCount();

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                if (j != 0)
                {
                    jtd += ",";
                }
                try
                {
                    jtd += getCSVField(jtData.getValueAt(i, j).toString());
                } catch (Exception e)
                {
                    e.printStackTrace();
                    System.out.println("Exception reading JTable value at ("
                            + i + ", " + j + "):\t" + e);
                }
            }
            jtd += "\n";
        }

        return jtd;
    }

    public void save()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(output, false);
            fos.write(exportData.getBytes());
            fos.close();
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Exception saving CSV File:\t" + e);
        }

    }

    public String getCSVData()
    {
        return exportData;
    }
}