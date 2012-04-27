package com.allthingscompute.sam.alert;

/**
 * This interface defines the common method - alertLevelChanged, for specific
 * information to each implementation see the classes that implement this
 * interface.
 * 
 * @author Sam, Documented by E. Internicola
 */
public interface AlertLevelChangeListener
{
    /**
     * This method defines the interface for an Alert Level Change. Each
     * subclass of AlertLevelChangeListener object must handle this event in
     * their own way.
     */
    public void alertLevelChanged(int alertLevel);
}