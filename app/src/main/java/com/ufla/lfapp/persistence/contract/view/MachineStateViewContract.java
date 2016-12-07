package com.ufla.lfapp.persistence.contract.view;

import com.ufla.lfapp.persistence.contract.table.MachineTransitionContract;
import com.ufla.lfapp.persistence.contract.table.StateContract;
import com.ufla.lfapp.persistence.contract.table.TransitionContract;

/**
 * Created by carlos on 12/7/16.
 */

public class MachineStateViewContract {

    private MachineStateViewContract() {

    }

    public static final String VIEW_NAME = "MachineStateView";

    /* "CREATE VIEW MachineStateView AS
            SELECT DISTINCT MachineTransition.Machine AS Machine, State.Name AS State FROM MachineTransition
            INNER JOIN Transition ON MachineTransition.Transition = Transition._id
            INNER JOIN State ON Transition.CurrentState = State._id OR Transition.FutureState = State._id
            ORDER BY Machine, State;"
    */
    public static final String CREATE_VIEW = "CREATE VIEW " + VIEW_NAME + " AS "
            + "SELECT DISTINCT " + MachineTransitionContract.TABLE_NAME + "."
                + MachineTransitionContract.Columns.MACHINE + " AS " + Columns.MACHINE + ", "
                + StateContract.TABLE_NAME + "." + StateContract.Columns.NAME + " AS "
                + Columns.STATE + " FROM " + MachineTransitionContract.TABLE_NAME + " "
            + "INNER JOIN " + TransitionContract.TABLE_NAME + " ON "
                + MachineTransitionContract.TABLE_NAME + "."
                + MachineTransitionContract.Columns.TRANSITION + " = "
                + TransitionContract.TABLE_NAME + "." + TransitionContract.Columns._ID + " "
            + "INNER JOIN " + StateContract.TABLE_NAME + " ON "
                + TransitionContract.TABLE_NAME + "." + TransitionContract.Columns.CURRENT_STATE
                + " = " + StateContract.TABLE_NAME + "." + StateContract.Columns._ID + " OR "
                + TransitionContract.TABLE_NAME + "." + TransitionContract.Columns.FUTURE_STATE
                + " = " + StateContract.TABLE_NAME + "." + StateContract.Columns._ID + " "
            + "ORDER BY " + Columns.MACHINE + ", " + Columns.STATE + ";";

    public static abstract class Columns {
        public static final String MACHINE = "Machine";
        public static final String STATE = "State";
    }
}
