/*
 * SortTracker.java
 *
 * Created on March 17, 2004, 1:37 PM
 */

package com.allthingscompute.sam.gui.Tools;

import com.allthingscompute.sam.database.Objects.AttackComparator;

/**
 * This class is used to centralize the sorting functionality of any of the
 * Attack Tables.
 * 
 * @author E. Internicola
 */
public class SortTracker
{
    private AttackComparator ac;

    /**
     * Default Constructor - this constructor initializes the AttackComparator
     * object to
     * {@link #com.allthingscompute.sam.database.Objects.AttackComparator.SORT_TIMESTAMP AttackComparator.SORT_TIMESTAMP}.
     */
    public SortTracker()
    {
        ac = new AttackComparator(AttackComparator.SORT_TIMESTAMP, true);
    }

    /**
     * Constructor that takes an existing AttackComparator.
     * 
     * @param ac -
     *            the initial AttackComparator (initial sorting field and
     *            order[asc,desc]);
     */
    public SortTracker(AttackComparator ac)
    {
        setAttackComparator(ac);
    }

    public void setAttackComparator(AttackComparator ac)
    {
        this.ac = ac;
    }

    public AttackComparator getAttackComparator()
    {
        return ac;
    }

    /**
     * This method does the actual sorting based on the depicted column.
     */
    public void sortAttacks(int col)
    {
        boolean ascend = true;
        switch (col)
        {
        case AttackColumnBuilder.COL_ATTACK_TYPE:
            if (ac.getCField() == AttackComparator.SORT_SIGNATURE_NAME)
            {
                ascend = !ac.getInvert();
            }
            ac = new AttackComparator(AttackComparator.SORT_SIGNATURE_NAME,
                    ascend);
            break;
        case AttackColumnBuilder.COL_COUNT_ID:
            if (ac.getCField() == AttackComparator.SORT_CID)
            {
                ascend = !ac.getInvert();
            }
            ac = new AttackComparator(AttackComparator.SORT_CID, ascend);
            break;
        case AttackColumnBuilder.COL_DESTINATION_ADDRESS:
            if (ac.getCField() == AttackComparator.SORT_DESTINATION_NAME)
            {
                ascend = !ac.getInvert();
            }
            ac = new AttackComparator(AttackComparator.SORT_DESTINATION_NAME,
                    ascend);
            break;
        case AttackColumnBuilder.COL_DESTINATION_IP:
            if (ac.getCField() == AttackComparator.SORT_DESTINATION_IP)
            {
                ascend = !ac.getInvert();
            }
            ac = new AttackComparator(AttackComparator.SORT_DESTINATION_IP,
                    ascend);
            break;
        case AttackColumnBuilder.COL_SENSOR_ADDRESS:
            if (ac.getCField() == AttackComparator.SORT_SENSOR_ADDRESS)
            {
                ascend = !ac.getInvert();
            }
            ac = new AttackComparator(AttackComparator.SORT_SENSOR_ADDRESS,
                    ascend);
            break;
        case AttackColumnBuilder.COL_SENSOR_ID:
            if (ac.getCField() == AttackComparator.SORT_SENSOR_ID)
            {
                ascend = !ac.getInvert();
            }
            ac = new AttackComparator(AttackComparator.SORT_SENSOR_ID, ascend);
            break;
        case AttackColumnBuilder.COL_SENSOR_INTERFACE:
            if (ac.getCField() == AttackComparator.SORT_SENSOR_INTERFACE)
            {
                ascend = !ac.getInvert();
            }
            ac = new AttackComparator(AttackComparator.SORT_SENSOR_INTERFACE,
                    ascend);
            break;
        case AttackColumnBuilder.COL_SIGNATURE_ID:
            if (ac.getCField() == AttackComparator.SORT_SIGNATURE_SIG_ID)
            {
                ascend = !ac.getInvert();
            }
            ac = new AttackComparator(AttackComparator.SORT_SIGNATURE_SIG_ID,
                    ascend);
            break;
        case AttackColumnBuilder.COL_SOURCE_ADDRESS:
            if (ac.getCField() == AttackComparator.SORT_SOURCE_NAME)
            {
                ascend = !ac.getInvert();
            }
            ac = new AttackComparator(AttackComparator.SORT_SOURCE_NAME, ascend);
            break;
        case AttackColumnBuilder.COL_SOURCE_IP:
            if (ac.getCField() == AttackComparator.SORT_SOURCE_IP)
            {
                ascend = !ac.getInvert();
            }
            ac = new AttackComparator(AttackComparator.SORT_SOURCE_IP, ascend);
            break;
        case AttackColumnBuilder.COL_TIMESTAMP:
            if (ac.getCField() == AttackComparator.SORT_TIMESTAMP)
            {
                ascend = !ac.getInvert();
            }
            ac = new AttackComparator(AttackComparator.SORT_TIMESTAMP, ascend);
            break;
        }
    }
}