package com.ufla.lfapp.persistence.contract.table;

import android.provider.BaseColumns;

/**
 * Created by carlos on 12/7/16.
 */

public class MachineStatePositionContract {

    private MachineStatePositionContract() {

    }

    public static final String TABLE_NAME = "MachineStatePosition";

    /* CREATE TABLE MachineStatePosition (
            _id INTEGER PRIMARY KEY NOT NULL,
            Machine INTEGER NOT NULL,
            State INTEGER NOT NULL,
            Position INTEGER NOT NULL,
            UNIQUE (Machine, State) ON CONFLICT ABORT,
            UNIQUE (Machine, Position) ON CONFLICT ABORT,
            FOREIGN KEY (Machine) REFERENCES Machine(_id) ON DELETE CASCADE ON UPDATE CASCADE,
            FOREIGN KEY (State) REFERENCES State(_id) ON DELETE RESTRICT ON UPDATE RESTRICT,
            FOREIGN KEY (Position) REFERENCES GridPosition(_id) ON DELETE RESTRICT ON UPDATE RESTRICT);"
    */
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + Columns._ID + " INTEGER PRIMARY KEY NOT NULL, "
            + Columns.MACHINE + " INTEGER NOT NULL, "
            + Columns.STATE + " INTEGER NOT NULL, "
            + Columns.POSITION + " INTEGER NOT NULL, "
            + "UNIQUE (" + Columns.MACHINE + ", " + Columns.STATE + ") ON CONFLICT ABORT, "
            + "UNIQUE (" + Columns.MACHINE + ", " + Columns.POSITION + ") ON CONFLICT ABORT, "
            + "FOREIGN KEY (" + Columns.MACHINE + ") REFERENCES " + MachineContract.TABLE_NAME
                + "(" + MachineContract.Columns._ID + ") ON DELETE CASCADE ON UPDATE CASCADE, "
            + "FOREIGN KEY (" + Columns.STATE + ") REFERENCES " + StateContract.TABLE_NAME
                + "(" +  StateContract.Columns._ID + ") ON DELETE RESTRICT ON UPDATE RESTRICT, "
            + "FOREIGN KEY (" + Columns.POSITION + ") REFERENCES " + GridPositionContract.TABLE_NAME
                + "(" +  GridPositionContract.Columns._ID + ") ON DELETE RESTRICT ON UPDATE RESTRICT);";

    public static abstract class Columns implements BaseColumns {
        public static final String _ID = BaseColumns._ID;
        public static final String MACHINE = "Machine";
        public static final String STATE = "State";
        public static final String POSITION = "Position";
    }
}
