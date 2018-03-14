package com.ufla.lfapp.persistence.contract.table;

import android.provider.BaseColumns;

/**
 * Created by carlos on 12/14/16.
 */

public class MachineDotLanguageContract {

    private MachineDotLanguageContract() {

    }

    public static final String TABLE_NAME = "MachineDotLanguage";

    /* "CREATE TABLE MachineDotLanguage (
            _id INTEGER PRIMARY KEY NOT NULL,
            DotLanguage TEXT NOT NULL,
            Label TEXT,
            CreationTime TEXT NOT NULL,
            ContUses INTEGER DEFAULT 1
            MachineType INTEGER NOT NULL);"
    */

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + MachineDotLanguageContract.Columns._ID + " INTEGER PRIMARY KEY NOT NULL, "
            + MachineDotLanguageContract.Columns.DOT_LANGUAGE + " TEXT NOT NULL, "
            + MachineDotLanguageContract.Columns.LABEL + " TEXT, "
            + MachineDotLanguageContract.Columns.CREATION_TIME + " INTEGER NOT NULL, "
            + MachineDotLanguageContract.Columns.CONT_USES + " INTEGER DEFAULT 1, "
            + MachineDotLanguageContract.Columns.MACHINE_TYPE + " INTEGER NOT NULL);";

    public static abstract class Columns implements BaseColumns {
        public static final String _ID = BaseColumns._ID;
        public static final String DOT_LANGUAGE = "DotLanguage";
        public static final String LABEL = "Label";
        public static final String CREATION_TIME = "CreationTime";
        public static final String CONT_USES = "ContUses";
        public static final String MACHINE_TYPE = "MachineType";
    }
}
