package com.allthingscompute.sam;

import com.allthingscompute.sam.gui.MainFrame;

/**
 * This class is where the main thread is created. All it does is create a
 * com.allthingscomput.sam.gui.MainFrame object
 * 
 * @author sfreiberg
 * @author Documented by E. Internicola
 * @see com.allthingscompute.sam.gui.MainFrame
 */
public class Sam
{
    /**
     * Create a {@link com.allthingscompute.sam.gui.MainFrame MainFrame}object,
     * and let the user take care of the rest.
     */
    public static void main(String[] args)
    {
        /*
         * try { new HelpFrame(); } catch( Exception e ) { System.out.println(
         * "Exception:\t" + e ); }
         */
        new MainFrame();
        //ColumnConfiguration cc = new ColumnConfiguration();
        //new PropertiesFrame();
    }

}