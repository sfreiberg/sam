/*
 * MenuBuilder.java
 *
 * Created on March 11, 2004, 11:02 AM
 */

package com.allthingscompute.sam.gui.Tools;

import javax.swing.JMenuItem;

/**
 * 
 * @author E. Internicola
 */
public class MenuBuilder
{
    public static final int ATTACK_MENU_SIZE = 14;

    public static final int FILTER_MENU_SIZE = 10;

    public static final int SORT_MENU_SIZE = 11;

    public static final int ATTACK_MENU_SHOW_DETAILS = 0;

    public static final int ATTACK_MENU_SHOW_DATA = 1;

    public static final int ATTACK_MENU_SHOW_SOURCE_HOSTNAME = 2;

    public static final int ATTACK_MENU_SHOW_DEST_HOSTNAME = 3;

    public static final int ATTACK_MENU_RELATED_ATTACKS_24 = 4;

    public static final int ATTACK_MENU_RELATED_ATTACKS_1 = 5;

    public static final int ATTACK_MENU_RELATED_SOURCE_24 = 6;

    public static final int ATTACK_MENU_RELATED_SOURCE_1 = 7;

    public static final int ATTACK_MENU_RELATED_DEST_24 = 8;

    public static final int ATTACK_MENU_RELATED_DEST_1 = 9;

    public static final int ATTACK_MENU_CSV_EXPORT = 10;

    public static final int ATTACK_MENU_LOOKUP_ALL = 11;

    public static final int ATTACK_MENU_SHOW_HIDE_COLUMNS = 12;

    public static final int ATTACK_MENU_REMOVE_FILTERS = 13;

    public static JMenuItem[] getAttackMenu()
    {
        JMenuItem[] menuItems = new JMenuItem[ATTACK_MENU_SIZE];

        menuItems[ATTACK_MENU_SHOW_DETAILS] = new JMenuItem("Show details");
        menuItems[ATTACK_MENU_SHOW_DATA] = new JMenuItem("Show data");
        menuItems[ATTACK_MENU_SHOW_SOURCE_HOSTNAME] = new JMenuItem(
                "Show source hostname");
        menuItems[ATTACK_MENU_SHOW_DEST_HOSTNAME] = new JMenuItem(
                "Show destination hostname");
        menuItems[ATTACK_MENU_RELATED_ATTACKS_24] = new JMenuItem(
                "Show related attacks - last 24 hours");
        menuItems[ATTACK_MENU_RELATED_ATTACKS_1] = new JMenuItem(
                "Show related attacks - last hour");
        menuItems[ATTACK_MENU_RELATED_SOURCE_24] = new JMenuItem(
                "Show related source attacks - last 24 hours");
        menuItems[ATTACK_MENU_RELATED_SOURCE_1] = new JMenuItem(
                "Show related source attacks - last hour");
        menuItems[ATTACK_MENU_RELATED_DEST_24] = new JMenuItem(
                "Show related dest attacks - last 24 hours");
        menuItems[ATTACK_MENU_RELATED_DEST_1] = new JMenuItem(
                "Show related dest attacks - last hour");
        menuItems[ATTACK_MENU_CSV_EXPORT] = new JMenuItem(
                "Export Attacks to CSV File");
        menuItems[ATTACK_MENU_LOOKUP_ALL] = new JMenuItem(
                "Force Lookup of Attack Hosts");
        menuItems[ATTACK_MENU_SHOW_HIDE_COLUMNS] = new JMenuItem(
                "Show/Hide Columns");
        menuItems[ATTACK_MENU_REMOVE_FILTERS] = new JMenuItem(
                "Remove All Filters");
        return menuItems;
    }

    public static JMenuItem[] getFilterMenu()
    {
        JMenuItem[] filterMenu = new JMenuItem[FILTER_MENU_SIZE];

        filterMenu[0] = new JMenuItem("Show only this Sensor Interface");
        filterMenu[1] = new JMenuItem("Exclude this Sensor Interface");
        filterMenu[2] = new JMenuItem("Show only this Sensor Address");
        filterMenu[3] = new JMenuItem("Exclude this Sensor Address");
        filterMenu[4] = new JMenuItem("Show only this Source");
        filterMenu[5] = new JMenuItem("Exclude this Source");
        filterMenu[6] = new JMenuItem("Show only this Destination");
        filterMenu[7] = new JMenuItem("Exclude this Destination");
        filterMenu[8] = new JMenuItem("Show only this Attack Type");
        filterMenu[9] = new JMenuItem("Exclude this Attack Type");

        return filterMenu;
    }
}