package com.ufla.lfapp.persistence.contract.view;

import com.ufla.lfapp.persistence.contract.table.StateContract;

/**
 * Created by carlos on 12/7/16.
 */

public class MachineTransitionViewContract {

    private MachineTransitionViewContract() {

    }

    public static final String VIEW_NAME = "MachineTransitionView";

    /* "CREATE VIEW MachineTransitionView AS
            SELECT Machine, CurrentState, Symbol, State.Name AS FutureState FROM
                MachineTransitionPartialView
            INNER JOIN State ON MachineTransitionPartialView.FutureStateId = State._id
            ORDER BY Machine, CurrentState, Symbol, FutureState;"
    */
    public static final String CREATE_VIEW = "CREATE VIEW " + VIEW_NAME + " AS "
            + "SELECT " + Columns.MACHINE + ", " + Columns.CURRENT_STATE + ", " + Columns.SYMBOL
                + ", " + StateContract.TABLE_NAME + "." + StateContract.Columns.NAME + " AS "
                + Columns.FUTURE_STATE + " FROM " + MachineTransitionPartialViewContract.VIEW_NAME + " "
            + "INNER JOIN " + StateContract.TABLE_NAME + " ON "
                + MachineTransitionPartialViewContract.VIEW_NAME + "."
                + MachineTransitionPartialViewContract.Columns.FUTURE_STATE_ID + " = "
                + StateContract.TABLE_NAME + "." + StateContract.Columns._ID + " "
            + "ORDER BY " + Columns.MACHINE + ", " + Columns.CURRENT_STATE + ", " + Columns.SYMBOL
                + ", " + Columns.FUTURE_STATE + ";";

    public static abstract class Columns {
        public static final String MACHINE = "Machine";
        public static final String CURRENT_STATE = "CurrentState";
        public static final String SYMBOL = "Symbol";
        public static final String FUTURE_STATE = "FutureState";
    }
}
