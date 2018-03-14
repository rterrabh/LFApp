package com.ufla.lfapp.persistence.contract.table;

import android.provider.BaseColumns;

/**
 * Created by carlos on 12/7/16.
 */

public class TransitionContract {

    private TransitionContract() {

    }

    public static final String TABLE_NAME = "Transition";

    /* "CREATE TABLE Transition (
            _id INTEGER PRIMARY KEY NOT NULL,
            CurrentState INTEGER NOT NULL,
            Symbol INTEGER NOT NULL,
            FutureState INTEGER NOT NULL,
            UNIQUE (CurrentState, Symbol, FutureState) ON CONFLICT ABORT,
            FOREIGN KEY (CurrentState) REFERENCES State(_id) ON DELETE RESTRICT ON UPDATE RESTRICT,
            FOREIGN KEY (Symbol) REFERENCES Symbol(_id) ON DELETE RESTRICT ON UPDATE RESTRICT,
            FOREIGN KEY (FutureState) REFERENCES State(_id) ON DELETE RESTRICT ON UPDATE RESTRICT);"
    */
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + Columns._ID + " INTEGER PRIMARY KEY NOT NULL, "
            + Columns.CURRENT_STATE + " INTEGER NOT NULL, "
            + Columns.SYMBOL + " INTEGER NOT NULL, "
            + Columns.FUTURE_STATE + " INTEGER NOT NULL, "
            + "UNIQUE (" + Columns.CURRENT_STATE + ", " + Columns.SYMBOL + ", "
                + Columns.FUTURE_STATE + ") ON CONFLICT ABORT, "
            + "FOREIGN KEY (" + Columns.CURRENT_STATE + ") REFERENCES " + StateContract.TABLE_NAME
                + "(" + StateContract.Columns._ID + ") ON DELETE RESTRICT ON UPDATE RESTRICT, "
            + "FOREIGN KEY (" + Columns.SYMBOL + ") REFERENCES " + SymbolContract.TABLE_NAME
                + "(" + SymbolContract.Columns._ID + ") ON DELETE RESTRICT ON UPDATE RESTRICT, "
            + "FOREIGN KEY (" + Columns.FUTURE_STATE + ") REFERENCES " + StateContract.TABLE_NAME
                + "(" + StateContract.Columns._ID + ") ON DELETE RESTRICT ON UPDATE RESTRICT);";

    public static final String INDEX_NAME = "TransitionIndex";

    /* "CREATE UNIQUE INDEX TransitionIndex ON Transition(CurrentState, Symbol, FutureState);"
     */
    public static final String CREATE_INDEX = "CREATE UNIQUE INDEX " + INDEX_NAME + " ON "
            + TABLE_NAME + "(" + Columns.CURRENT_STATE + ", " + Columns.SYMBOL + ", "
            + Columns.FUTURE_STATE + ");";

    public static abstract class Columns implements BaseColumns {
        public static final String _ID = BaseColumns._ID;
        public static final String CURRENT_STATE = "CurrentState";
        public static final String SYMBOL = "Symbol";
        public static final String FUTURE_STATE = "FutureState";
    }
}
