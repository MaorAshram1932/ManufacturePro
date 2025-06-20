package com.suppliq.manufacturepro.Utils;

import javafx.scene.control.TableColumn;

public class ColumnWidths {
    // טקסטים
    public static final double SHORT_TXT = 70;
    public static final double MID_TXT = 120;
    public static final double LONG_TXT = 250;

    // מספרים
    public static final double SHORT_NUM = 50;
    public static final double MID_NUM = 80;
    public static final double LONG_NUM = 100;

    // עמודת תיאור
    public static final double DESCRIPTION_DEFAULT = 250;
    public static final double DESCRIPTION_MIN = 160;

    // עמודת כפתורים
    public static final double ACTIONS_DEFAULT = 180;
    public static final double ACTIONS = 180;

    // שימושי
    public static <T> void setFixedWidth(TableColumn<T, ?> column, double width) {
        column.setPrefWidth(width);
        column.setMinWidth(width);
        column.setMaxWidth(width);
    }

    public static <T> void setSmartWidth(TableColumn<T, ?> column, double pref, double min) {
        column.setPrefWidth(pref);
        column.setMinWidth(min);
    }

    public static <T> void setFlexibleWidth(TableColumn<T, ?> column, double pref) {
        column.setPrefWidth(pref);
    }
}
