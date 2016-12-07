package com.ufla.lfapp.persistence.contract.view;

import com.ufla.lfapp.persistence.contract.table.GridPositionContract;
import com.ufla.lfapp.persistence.contract.table.MachineStatePositionContract;
import com.ufla.lfapp.persistence.contract.table.StateContract;

/**
 * Created by carlos on 12/7/16.
 */

public class MachineStatePositionViewContract {

    private MachineStatePositionViewContract() {

    }

    public static final String VIEW_NAME = "MachineStatePositionView";

    /* "CREATE VIEW MachineStatePositionView AS
            SELECT MachineStatePosition.Machine AS Machine, State.Name AS State, GridPosition.X
                AS X, GridPosition.Y AS Y FROM MachineStatePosition
            INNER JOIN State ON MachineStatePosition.State = State._id
            INNER JOIN GridPosition ON MachineStatePosition.Position = GridPosition._id
            ORDER BY Machine, State;"
    */
    public static final String CREATE_VIEW = "CREATE VIEW " + VIEW_NAME + " AS "
            + "SELECT " + MachineStatePositionContract.TABLE_NAME + "."
                + MachineStatePositionContract.Columns.MACHINE + " AS " + Columns.MACHINE + ", "
                + StateContract.TABLE_NAME + "." + StateContract.Columns.NAME + " AS "
                + Columns.STATE + ", " + GridPositionContract.TABLE_NAME + "."
                + GridPositionContract.Columns.X + " AS " + Columns.X + ", "
                + GridPositionContract.TABLE_NAME + "." + GridPositionContract.Columns.Y + " AS "
                + Columns.Y + " FROM " + MachineStatePositionContract.TABLE_NAME + " "
            + "INNER JOIN " + StateContract.TABLE_NAME + " ON "
                + MachineStatePositionContract.TABLE_NAME + "."
                + MachineStatePositionContract.Columns.STATE + " = "
                + StateContract.TABLE_NAME + "." + StateContract.Columns._ID + " "
            + "INNER JOIN " + GridPositionContract.TABLE_NAME + " ON "
                + MachineStatePositionContract.TABLE_NAME + "."
                + MachineStatePositionContract.Columns.POSITION + " = "
                + GridPositionContract.TABLE_NAME + "." + GridPositionContract.Columns._ID + " "
            + "ORDER BY " + Columns.MACHINE + ", " + Columns.STATE + ";";

    public static abstract class Columns {
        public static final String MACHINE = "Machine";
        public static final String STATE = "State";
        public static final String X = "X";
        public static final String Y = "Y";
    }
}
