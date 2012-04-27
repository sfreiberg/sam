/*
 * AttackVectorFilter.java
 *
 * Created on March 11, 2004, 9:47 AM
 */

package com.allthingscompute.sam.database.Objects;

import com.allthingscompute.sam.database.Filter.AttackFilter;
import com.allthingscompute.sam.database.Filter.AttackFilterVector;

/**
 * This class is used to take a given Attack Vector, and apply the logic of a
 * Vector of Attack Filters to that Attack Vector and come up with an Attack
 * Vector that is a subset of the original Attack Vector, minus any Attacks that
 * should be filtered out. <BR>
 * <BR>
 * Usage: <BR>
 * AttackVectorFilter avf = new AttackVectorFilter( attackVector,
 * attackFilterVector );<BR>
 * afv.applyFilters(); <BR>
 * //now display the filters <BR>
 * AttackVector visibleAttackVector = afv.getVisibleAttackVector();
 * 
 * @author E. Internicola
 */
public class AttackVectorFilter
{
    private AttackVector av = null;

    private AttackVector visibleAV = null;

    private AttackFilterVector afv = null;

    public AttackVectorFilter()
    {
    }

    public AttackVectorFilter(AttackVector av, AttackFilterVector afv)
    {
        setAttackVector(av);
        setAttackFilterVector(afv);
    }

    public void setAttackVector(AttackVector av)
    {
        this.av = av;
    }

    public void setAttackFilterVector(AttackFilterVector afv)
    {
        this.afv = afv;
    }

    /**
     * This method will return the Visible Attack Vector.
     */
    public AttackVector getVisibleAttackVector()
    {
        return visibleAV;
    }

    /**
     * This method will use the Attack Filter properties associated with this
     * class to determine wether or not a particular attack should show up in
     * the list or not. A filter that filters out an attack will always take
     * precedence over one that will show an attack. Thus looking at the code,
     * you will notice that a filter that filters out an attack will always
     * return a false value immediately, whereas a filter that includes the
     * attack waits for all of the filters to be checked before returning true.
     */
    private boolean addAttack(Attack a)
    {
        boolean show = true;
        for (int i = 0; i < afv.size(); i++)
        {
            AttackFilter af = (AttackFilter) afv.elementAt(i);
            int ff = af.getFilterField();
            int ft = af.getFilterType();
            String fv = af.getFilterValue();

            if (ff == AttackFilter.FIELD_ATTACK_SIGNATURE)
            {
                if (ft == AttackFilter.FILTER_HIDE
                        && !fv.equals(a.getSignature().getSigName())
                        || ft == AttackFilter.FILTER_SHOW
                        && fv.equals(a.getSignature().getSigName()))
                {
                    show = true;
                } else if (ft == AttackFilter.FILTER_HIDE
                        && fv.equals(a.getSignature().getSigName())
                        || ft == AttackFilter.FILTER_SHOW
                        && !fv.equals(a.getSignature().getSigName()))
                {
                    return false;
                }

            } else if (ff == AttackFilter.FIELD_DESTINATION)
            {
                if (ft == AttackFilter.FILTER_HIDE
                        && !fv.equals(a.getDestination().toString())
                        || ft == AttackFilter.FILTER_SHOW
                        && fv.equals(a.getDestination().toString()))
                {
                    show = true;
                } else if (ft == AttackFilter.FILTER_HIDE
                        && fv.equals(a.getDestination().toString())
                        || ft == AttackFilter.FILTER_SHOW
                        && !fv.equals(a.getDestination().toString()))
                {
                    return false;
                }
            } else if (ff == AttackFilter.FIELD_SENSOR_ADDRESS)
            {
                if (ft == AttackFilter.FILTER_HIDE
                        && !fv.equals(a.getSensor().getHost().getIP())
                        || ft == AttackFilter.FILTER_SHOW
                        && fv.equals(a.getSensor().getHost().getIP()))
                {
                    show = true;
                } else if (ft == AttackFilter.FILTER_HIDE
                        && fv.equals(a.getSensor().getHost().getIP())
                        || ft == AttackFilter.FILTER_SHOW
                        && !fv.equals(a.getSensor().getHost().getIP()))
                {
                    return false;
                }
            } else if (ff == AttackFilter.FIELD_SENSOR_INTERFACE)
            {
                if (ft == AttackFilter.FILTER_HIDE
                        && !fv.equals(a.getSensor().getInterface())
                        || ft == AttackFilter.FILTER_SHOW
                        && fv.equals(a.getSensor().getInterface()))
                {
                    show = true;
                } else if (ft == AttackFilter.FILTER_HIDE
                        && fv.equals(a.getSensor().getInterface())
                        || ft == AttackFilter.FILTER_SHOW
                        && !fv.equals(a.getSensor().getInterface()))
                {
                    return false;
                }
            } else if (ff == AttackFilter.FIELD_SOURCE)
            {
                if (ft == AttackFilter.FILTER_HIDE
                        && !fv.equals(a.getSource().toString())
                        || ft == AttackFilter.FILTER_SHOW
                        && fv.equals(a.getSource().toString()))
                {
                    show = true;
                } else if (ft == AttackFilter.FILTER_HIDE
                        && fv.equals(a.getSource().toString())
                        || ft == AttackFilter.FILTER_SHOW
                        && !fv.equals(a.getSource().toString()))
                {
                    return false;
                }
            } else if (ff == AttackFilter.FIELD_TIMESTAMP)
            {
                if (ft == AttackFilter.FILTER_HIDE
                        && !fv.equals(a.getTimestamp())
                        || ft == AttackFilter.FILTER_SHOW
                        && fv.equals(a.getTimestamp()))
                {
                    show = true;
                } else if (ft == AttackFilter.FILTER_HIDE
                        && fv.equals(a.getTimestamp())
                        || ft == AttackFilter.FILTER_SHOW
                        && !fv.equals(a.getTimestamp()))
                {
                    return false;
                }
            }
        }
        return show;
    }

    /**
     * This method sets the AttackVector and the AttackFilterVector before
     * making a call to the applyFilters method that takes no parameters.
     * 
     * @see #applyFilters
     */
    public void applyFilters(AttackVector av, AttackFilterVector afv)
    {
        setAttackVector(av);
        setAttackFilterVector(afv);
        applyFilters();
    }

    /**
     * This method populates the filtered AttackVector: visibleAV using the
     * Filters and the Attack Vector: av.
     */
    public void applyFilters()
    {
        Attack a = null;
        visibleAV = new AttackVector();
        for (int i = 0; i < av.size(); i++)
        {
            a = (Attack) av.elementAt(i);
            if (addAttack(a))
            {
                visibleAV.add(a);
            }
        }
    }

}