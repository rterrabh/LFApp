package com.ufla.lfapp.persistence.contract.table;

import android.provider.BaseColumns;

/**
 * Created by carlos on 12/7/16.
 */

public class MachineFinalStateContract {

    private MachineFinalStateContract() {

    }

    public static final String TABLE_NAME = "MachineFinalState";

    /* "CREATE TABLE MachineFinalState (
            _id INTEGER PRIMARY KEY NOT NULL,
            Machine INTEGER NOT NULL,
            State INTEGER NOT NULL,
            UNIQUE (Machine, State) ON CONFLICT ABORT,
            FOREIGN KEY (Machine) REFERENCES Machine(_id) ON DELETE CASCADE ON UPDATE CASCADE,
            FOREIGN KEY (State) REFERENCES State(_id) ON DELETE RESTRICT ON UPDATE RESTRICT);"
    */
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + Columns._ID + " INTEGER PRIMARY KEY NOT NULL, "
            + Columns.MACHINE + " INTEGER NOT NULL, "
            + Columns.STATE + " INTEGER NOT NULL, "
            + "UNIQUE (" + Columns.MACHINE + ", " + Columns.STATE + ") ON CONFLICT ABORT, "
            + "FOREIGN KEY (" + Columns.MACHINE + ") REFERENCES " + MachineContract.TABLE_NAME
                + "(" + MachineContract.Columns._ID + ") ON DELETE CASCADE ON UPDATE CASCADE, "
            + "FOREIGN KEY (" + Columns.STATE + ") REFERENCES " + StateContract.TABLE_NAME
                + "(" + StateContract.Columns._ID + ") ON DELETE RESTRICT ON UPDATE RESTRICT);";

    public static abstract class Columns implements BaseColumns {
        public static final String _ID = BaseColumns._ID;
        public static final String MACHINE = "Machine";
        public static final String STATE = "State";
    }

}
