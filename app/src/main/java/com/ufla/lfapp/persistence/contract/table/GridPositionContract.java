package com.ufla.lfapp.persistence.contract.table;

import android.provider.BaseColumns;

/**
 * Created by carlos on 12/7/16.
 */

public class GridPositionContract {

    private GridPositionContract() {

    }

    public static final String TABLE_NAME = "GridPosition";

    /* "CREATE TABLE GridPosition (
            _id INTEGER PRIMARY KEY NOT NULL,
            X INTEGER NOT NULL,
            Y INTEGER NOT NULL,
            UNIQUE (X, Y) ON CONFLICT ABORT);"
    */
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + Columns._ID + " INTEGER PRIMARY KEY NOT NULL, "
            + Columns.X + " INTEGER NOT NULL, "
            + Columns.Y + " INTEGER NOT NULL, "
            + "UNIQUE (" + Columns.X + ", " + Columns.Y + ") ON CONFLICT ABORT);";

    public static final String INDEX_NAME = "GridPositionIndex";

    /* "CREATE UNIQUE INDEX GridPositionIndex ON GridPosition(X, Y);"
     */
    public static final String CREATE_INDEX = "CREATE UNIQUE INDEX " + INDEX_NAME + " ON "
            + TABLE_NAME + "(" + Columns.X + ", " + Columns.Y + ");";

    public static abstract class Columns implements BaseColumns {
        public static final String _ID = BaseColumns._ID;
        public static final String X = "X";
        public static final String Y = "Y";
    }
}
