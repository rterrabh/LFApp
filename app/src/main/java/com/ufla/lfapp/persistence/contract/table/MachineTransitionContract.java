package com.ufla.lfapp.persistence.contract.table;

import android.provider.BaseColumns;

/**
 * Created by carlos on 12/7/16.
 */

public class MachineTransitionContract {

    private MachineTransitionContract() {

    }

    public static final String TABLE_NAME = "MachineTransition";

    /* "CREATE TABLE MachineTransition (
            _id INTEGER PRIMARY KEY NOT NULL,
            Machine INTEGER NOT NULL,
            Transition INTEGER NOT NULL,
            UNIQUE (Machine, Transition) ON CONFLICT ABORT,
            FOREIGN KEY (Machine) REFERENCES Machine(_id) ON DELETE CASCADE ON UPDATE CASCADE,
            FOREIGN KEY (Transition) REFERENCES Transition(_id) ON DELETE RESTRICT ON UPDATE RESTRICT);"
    */
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + Columns._ID + " INTEGER PRIMARY KEY NOT NULL, "
            + Columns.MACHINE + " INTEGER NOT NULL, "
            + Columns.TRANSITION + " INTEGER NOT NULL, "
            + "UNIQUE (" + Columns.MACHINE + ", " + Columns.TRANSITION + ") ON CONFLICT ABORT, "
            + "FOREIGN KEY (" + Columns.MACHINE + ") REFERENCES " + MachineContract.TABLE_NAME
            + "(" + MachineContract.Columns._ID + ") ON DELETE CASCADE ON UPDATE CASCADE, "
            + "FOREIGN KEY (" + Columns.TRANSITION + ") REFERENCES " + TransitionContract.TABLE_NAME
            + "(" + TransitionContract.Columns._ID + ") ON DELETE RESTRICT ON UPDATE RESTRICT);";

    public static abstract class Columns implements BaseColumns {
        public static final String _ID = BaseColumns._ID;
        public static final String MACHINE = "Machine";
        public static final String TRANSITION = "Transition";
    }
}
