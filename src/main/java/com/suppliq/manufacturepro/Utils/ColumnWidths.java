package com.suppliq.manufacturepro.Utils;

import javax.swing.table.TableColumn;

public class ColumnWidths {
    // רוחב קבוע לעמודות צפויות
    public static final double ID = 60;
    public static final double NAME = 150;
    public static final double PRICE = 80;
    public static final double STOCK = 70;
    public static final double CATEGORY = 110;
    public static final double ACTIONS = 180;

    // ערכים עבור עמודות גמישות
    public static final double DESCRIPTION_DEFAULT = 300;
    public static final double DESCRIPTION_MAX = 500;

    // תוספת למקרה של scrollbar
    public static final double SCROLLBAR_PADDING = 2;

    // חישוב דינמי של תיאור בהתבסס על רוחב הטבלה הכולל
    public static double getDescriptionWidth(double tableWidth) {
        return tableWidth
                - ID
                - NAME
                - PRICE
                - STOCK
                - CATEGORY
                - ACTIONS
                - SCROLLBAR_PADDING;
    }


}
