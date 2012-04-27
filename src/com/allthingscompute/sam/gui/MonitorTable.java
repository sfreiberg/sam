package com.allthingscompute.sam.gui;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.allthingscompute.sam.config.PropertiesManager;
import com.allthingscompute.sam.database.Objects.AttackVector;
import com.allthingscompute.sam.database.Objects.DBObjectVector;
import com.allthingscompute.sam.gui.Tools.AttackColumnBuilder;
import com.allthingscompute.sam.monitor.Monitor;
import com.allthingscompute.sam.monitor.MonitorChangeListener;

/**
 * This class is used to display the monitor table. It provides the base
 * functionality for the other tables that are subclasses of MonitorTable. <BR>
 * <BR>
 * <B>Direct Known Sub-Classes: </B> <BR>
 * {@link TopAttacks1HourTable TopAttacks1HourTable}<BR>
 * 
 * @author sfreiberg, Documented by E. Internicola <BR>
 *         Date Jul 3, 2002 <BR>
 *         <BR>
 */
public class MonitorTable implements MonitorChangeListener
{
    protected JTable table = null;

    protected DefaultTableModel model = null;

    protected DBObjectVector dbOV = null;

    protected int[] colNames = null;

    protected String[] strColNames = null;

    public MonitorTable()
    {
        loadColumns();
        model = new DefaultTableModel(AttackColumnBuilder
                .getColumnNames(colNames), 0)
        {
            public boolean isCellEditable(int row, int col)
            {
                return false;
            }
        };
        construct(AttackColumnBuilder.getColumnNames(colNames));
    }

    protected void loadColumns()
    {
        try
        {
            colNames = AttackColumnBuilder.stringToIntArray(PropertiesManager
                    .getPropertiesManager().getValue("AttackColumns"));
        } catch (Exception e)
        {
            colNames = AttackColumnBuilder.getAllColumnsAsInts();
            e.printStackTrace();
            System.out
                    .println("Exception reading Attack Columns from Properties.");
        }
    }

    /**
     *  
     */
    public MonitorTable(String[] strColNames)
    {
        model = new DefaultTableModel(strColNames, 0)
        {
            public boolean isCellEditable(int row, int col)
            {
                return false;
            }
        };
        construct(strColNames);
    }

    /**
     *  
     */
    public MonitorTable(int[] colNames)
    {
        model = new DefaultTableModel(AttackColumnBuilder
                .getColumnNames(colNames), 0)
        {
            public boolean isCellEditable(int row, int col)
            {
                return false;
            }
        };
        construct(colNames);
    }

    /**
     *  
     */
    protected void construct(int[] colNames)
    {
        this.colNames = colNames;
        table = new JTable(model);
        table.setShowGrid(false);
        table.validate();
    }

    /**
     *  
     */
    protected void setHeaders(String[] strColNames)
    {
        model = new DefaultTableModel(strColNames, 0)
        {
            public boolean isCellEditable(int row, int col)
            {
                return false;
            }
        };
        table.setModel(model);
        this.strColNames = strColNames;
        table.validate();
    }

    /**
     *  
     */
    private void construct(String[] strColNames)
    {
        this.strColNames = strColNames;
        table = new JTable(model);
        table.setShowGrid(false);
    }

    /**
     *  
     */
    public MonitorTable(int[] colNames, Vector v)
    {
        model = new DefaultTableModel(AttackColumnBuilder
                .getColumnNames(colNames), 0)
        {
            public boolean isCellEditable(int row, int col)
            {
                return false;
            }
        };
        setTableData(v);
        construct(colNames);
    }

    /**
     *  
     */
    public JTable getTable()
    {
        return table;
    }

    /**
     *  
     */
    public DefaultTableModel getModel()
    {
        return model;
    }

    public void reloadColumns()
    {
        loadColumns();
        setHeaders(AttackColumnBuilder.getColumnNames(colNames));
    }

    /**
     *  
     */
    public void setTableData(AttackVector av)
    {
        Vector v = AttackColumnBuilder.displayColumns(av, AttackColumnBuilder
                .getIntVectorFromInts(colNames));
        setTableData(v);
    }

    /**
     * This method will set the data of a table based on the data provided in
     * the vector.
     */
    public void setTableData(Vector data)
    {
        model.setRowCount(0);
        Enumeration keys = data.elements();
        while (keys.hasMoreElements())
        {
            Vector v = (Vector) keys.nextElement();
            model.addRow(v);
        }
    }

    /**
     * @see com.allthingscompute.sam.monitor.MonitorChangeListener#monitorChanged(Monitor)
     */
    public void monitorChanged(Monitor monitor)
    {
        //		TimedMonitor tMonitor = (TimedMonitor)monitor;
        //		setTableData(tMonitor.getTopAttackers());
    }

    /**
     *  
     */
    public DBObjectVector getDBObjectVector()
    {
        return this.dbOV;
    }

    /**
     *  
     */
    public void setDBObjectVector(DBObjectVector dbov)
    {
        this.dbOV = dbov;
    }
}