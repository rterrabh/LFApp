package com.ufla.lfapp.persistence.contract.view;

import com.ufla.lfapp.persistence.contract.table.MachineTransitionContract;
import com.ufla.lfapp.persistence.contract.table.StateContract;
import com.ufla.lfapp.persistence.contract.table.SymbolContract;
import com.ufla.lfapp.persistence.contract.table.TransitionContract;

/**
 * Created by carlos on 12/7/16.
 */

public class MachineTransitionPartialViewContract {

    private MachineTransitionPartialViewContract() {

    }

    public static final String VIEW_NAME = "MachineTransitionPartialView";

    /* "CREATE VIEW MachineTransitionPartialView AS
            SELECT MachineTransition.Machine AS Machine, State.Name AS CurrentState, Symbol.Name AS
                Symbol, Transition.FutureState AS FutureStateId FROM MachineTransition
            INNER JOIN Transition ON MachineTransition.Transition = Transition._id
            INNER JOIN State ON Transition.CurrentState = State._id
            INNER JOIN Symbol ON Transition.Symbol = Symbol._id;"
    */
    public static final String CREATE_VIEW = "CREATE VIEW " + VIEW_NAME + " AS "
            + "SELECT " + MachineTransitionContract.TABLE_NAME + "."
                + MachineTransitionContract.Columns.MACHINE + " AS " + Columns.MACHINE + ", "
                + StateContract.TABLE_NAME + "." + StateContract.Columns.NAME + " AS "
                + Columns.CURRENT_STATE + ", " + SymbolContract.TABLE_NAME + "."
                + SymbolContract.Columns.NAME + " AS " + Columns.SYMBOL + ", "
                + TransitionContract.TABLE_NAME + "." + TransitionContract.Columns.FUTURE_STATE + " AS "
                + Columns.FUTURE_STATE_ID + " FROM " + MachineTransitionContract.TABLE_NAME + " "
            + "INNER JOIN " + TransitionContract.TABLE_NAME + " ON "
                + MachineTransitionContract.TABLE_NAME + "."
                + MachineTransitionContract.Columns.TRANSITION + " = "
                + TransitionContract.TABLE_NAME + "." + TransitionContract.Columns._ID + " "
            + "INNER JOIN " + StateContract.TABLE_NAME + " ON "
                + TransitionContract.TABLE_NAME + "." + TransitionContract.Columns.CURRENT_STATE
                + " = " + StateContract.TABLE_NAME + "." + StateContract.Columns._ID + " "
            + "INNER JOIN " + SymbolContract.TABLE_NAME + " ON "
                + TransitionContract.TABLE_NAME + "." + TransitionContract.Columns.SYMBOL
                + " = " + SymbolContract.TABLE_NAME + "." + SymbolContract.Columns._ID + ";";

    public static abstract class Columns {
        public static final String MACHINE = "Machine";
        public static final String CURRENT_STATE = "CurrentState";
        public static final String SYMBOL = "Symbol";
        public static final String FUTURE_STATE_ID = "FutureStateId";
    }
}
