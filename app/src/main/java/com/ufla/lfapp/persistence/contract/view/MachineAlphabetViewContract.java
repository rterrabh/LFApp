package com.ufla.lfapp.persistence.contract.view;

import com.ufla.lfapp.persistence.contract.table.MachineTransitionContract;
import com.ufla.lfapp.persistence.contract.table.SymbolContract;
import com.ufla.lfapp.persistence.contract.table.TransitionContract;

/**
 * Created by carlos on 12/7/16.
 */

public class MachineAlphabetViewContract {

    private MachineAlphabetViewContract() {

    }

    public static final String VIEW_NAME = "MachineAlphabetView";

    /* "CREATE VIEW MachineAlphabetView AS
            SELECT DISTINCT MachineTransition.Machine AS Machine, Symbol.Name AS Alphabet FROM
                MachineTransition
            INNER JOIN Transition ON MachineTransition.Transition = Transition._id
            INNER JOIN Symbol ON Transition.Symbol = Symbol._id
            ORDER BY Machine, Alphabet;"
    */
    public static final String CREATE_VIEW = "CREATE VIEW " + VIEW_NAME + " AS "
            + "SELECT DISTINCT " + MachineTransitionContract.TABLE_NAME + "."
                + MachineTransitionContract.Columns.MACHINE + " AS " + Columns.MACHINE + ", "
                + SymbolContract.TABLE_NAME + "." + SymbolContract.Columns.NAME + " AS "
                + Columns.ALPHABET + " FROM " + MachineTransitionContract.TABLE_NAME + " "
            + "INNER JOIN " + TransitionContract.TABLE_NAME + " ON "
                + MachineTransitionContract.TABLE_NAME + "."
                + MachineTransitionContract.Columns.TRANSITION + " = "
                + TransitionContract.TABLE_NAME + "." + TransitionContract.Columns._ID + " "
            + "INNER JOIN " + SymbolContract.TABLE_NAME + " ON "
                + TransitionContract.TABLE_NAME + "." + TransitionContract.Columns.SYMBOL + " = "
                + SymbolContract.TABLE_NAME + "." + SymbolContract.Columns._ID + " "
            + "ORDER BY " + Columns.MACHINE + ", " + Columns.ALPHABET + ";";

    public static abstract class Columns {
        public static final String MACHINE = "Machine";
        public static final String ALPHABET = "Alphabet";
    }
}
