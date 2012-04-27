/*
 * FilterFrame.java
 *
 * Created on March 4, 2004, 9:43 PM
 */

package com.allthingscompute.sam.gui;

import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.allthingscompute.sam.database.Filter.AttackFilterVector;
import com.allthingscompute.sam.database.Objects.Attack;

/**
 * 
 * @author E. Internicola
 */
public class FilterFrame extends JFrame
{
    private AttackFilterVector afv = null;

    private Attack a = null;

    private JLabel[] labels = null;

    private JTextField[] textFields = null;

    private JCheckBox[] checkBoxes = null;

    public FilterFrame(AttackFilterVector afv, Attack a)
    {
        this.afv = afv;
        this.a = a;
        setSize(400, 200);
        this.getContentPane().setLayout(new GridLayout(afv.size() + 1, 3));

        //show( true );
    }

}