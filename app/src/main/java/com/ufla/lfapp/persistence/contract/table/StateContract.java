package com.ufla.lfapp.persistence.contract.table;

import android.provider.BaseColumns;

/**
 * Created by carlos on 12/7/16.
 */

public class StateContract {

    private StateContract() {

    }

    public static final String TABLE_NAME = "State";

    /* "CREATE TABLE State (
	        _id INTEGER PRIMARY KEY NOT NULL,
	        Name TEXT UNIQUE NOT NULL);"
    */
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + Columns._ID + " INTEGER PRIMARY KEY NOT NULL, "
            + Columns.NAME + " TEXT UNIQUE NOT NULL);";

    public static final String INDEX_NAME = "StateName";

    /* "CREATE UNIQUE INDEX StateName ON State(Name);"
     */
    public static final String CREATE_INDEX = "CREATE UNIQUE INDEX " + INDEX_NAME + " ON "
            + TABLE_NAME + "(" + Columns.NAME + ");";

    public static abstract class Columns implements BaseColumns {
        public static final String _ID = BaseColumns._ID;
        public static final String NAME = "Name";
    }
}
