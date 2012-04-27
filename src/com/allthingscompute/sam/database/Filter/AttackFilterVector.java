/*
 * AttackFilterVector.java
 *
 * Created on March 4, 2004, 9:02 PM
 */

package com.allthingscompute.sam.database.Filter;

import java.util.Vector;

/**
 * 
 * @author E. Internicola
 */
public class AttackFilterVector extends Vector
{

    public void addFilter(int filterField, String filterValue, int filterType)
    {
        add(new AttackFilter(filterField, filterValue, filterType));
    }

    public void addFilter(AttackFilter af)
    {
        if (af.getFilterValue() != null)
        {
            add(af);
        }
    }
}