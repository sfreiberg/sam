/*
 * FilterEditDialog.java
 *
 * Created on March 23, 2004, 3:39 PM
 */

package com.allthingscompute.sam.gui.Dialogs;

import java.awt.Frame;

import javax.swing.JDialog;

import com.allthingscompute.sam.database.Filter.AttackFilterVector;

/**
 * This class is used to edit a collection of filters.
 * 
 * @author E. Internicola
 */
public class FilterEditDialog extends JDialog
{
    AttackFilterVector afv = null;

    /** Creates a new instance of FilterEditDialog */
    public FilterEditDialog()
    {
        super();
    }

    public FilterEditDialog(Frame owner, AttackFilterVector afv)
    {
        super(owner, true);
        setAttackFilterVector(afv);
    }

    public FilterEditDialog(Frame owner, boolean modal, AttackFilterVector afv)
    {
        super(owner, modal);
        setAttackFilterVector(afv);
    }

    public void setAttackFilterVector(AttackFilterVector afv)
    {
        this.afv = afv;
    }

}