package com.ufla.lfapp.persistence.contract.table;

import android.provider.BaseColumns;

/**
 * Created by carlos on 12/7/16.
 */

public class MachineContract {

    private MachineContract() {

    }

    public static final String TABLE_NAME = "Machine";

    /* "CREATE TABLE Machine (
            _id INTEGER PRIMARY KEY NOT NULL,
            InitialState INTEGER,
            Label TEXT,
            CreationTime TEXT NOT NULL,
            ContUses INTEGER DEFAULT 1,
            FOREIGN KEY (InitialState) REFERENCES State(_id) ON DELETE RESTRICT ON UPDATE RESTRICT);"
    */
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + Columns._ID + " INTEGER PRIMARY KEY NOT NULL, "
            + Columns.INITIAL_STATE + " INTEGER, "
            + Columns.LABEL + " TEXT, "
            + Columns.CREATION_TIME + " INTEGER NOT NULL, "
            + Columns.CONT_USES + " INTEGER DEFAULT 1, "
            + "FOREIGN KEY (" + Columns.INITIAL_STATE + ") REFERENCES " + StateContract.TABLE_NAME
                + "(" + StateContract.Columns._ID + ") ON DELETE RESTRICT ON UPDATE RESTRICT);";

    public static abstract class Columns implements BaseColumns {
        public static final String _ID = BaseColumns._ID;
        public static final String INITIAL_STATE = "InitialState";
        public static final String LABEL = "Label";
        public static final String CREATION_TIME = "CreationTime";
        public static final String CONT_USES = "ContUses";
    }

}
