package com.ufla.lfapp.persistence.contract.view;

import com.ufla.lfapp.persistence.contract.table.MachineFinalStateContract;
import com.ufla.lfapp.persistence.contract.table.StateContract;

/**
 * Created by carlos on 12/7/16.
 */

public class MachineFinalStateViewContract {

    private MachineFinalStateViewContract() {

    }

    public static final String VIEW_NAME = "MachineFinalStateView";

    /* "CREATE VIEW MachineFinalStateView AS
            SELECT MachineFinalState.Machine AS Machine, State.Name AS State FROM MachineFinalState
            INNER JOIN State ON MachineFinalState.State = State._id
            ORDER BY Machine, State;"
    */
    public static final String CREATE_VIEW = "CREATE VIEW " + VIEW_NAME + " AS "
            + "SELECT " + MachineFinalStateContract.TABLE_NAME + "."
                + MachineFinalStateContract.Columns.MACHINE + " AS " + Columns.MACHINE + ", "
                + StateContract.TABLE_NAME + "." + StateContract.Columns.NAME + " AS "
                + Columns.STATE + " FROM " + MachineFinalStateContract.TABLE_NAME + " "
            + "INNER JOIN " + StateContract.TABLE_NAME + " ON "
                + MachineFinalStateContract.TABLE_NAME + "."
                + MachineFinalStateContract.Columns.STATE + " = "
                + StateContract.TABLE_NAME + "." + StateContract.Columns._ID + " "
            + "ORDER BY " + Columns.MACHINE + ", " + Columns.STATE + ";";

    public static abstract class Columns {
        public static final String MACHINE = "Machine";
        public static final String STATE = "State";
    }
}
