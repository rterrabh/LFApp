package com.ufla.lfapp.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ufla.lfapp.persistence.contract.table.GrammarContract;
import com.ufla.lfapp.persistence.contract.table.GridPositionContract;
import com.ufla.lfapp.persistence.contract.table.MachineContract;
import com.ufla.lfapp.persistence.contract.table.MachineDotLanguageContract;
import com.ufla.lfapp.persistence.contract.table.MachineFinalStateContract;
import com.ufla.lfapp.persistence.contract.table.MachineStatePositionContract;
import com.ufla.lfapp.persistence.contract.table.MachineTransitionContract;
import com.ufla.lfapp.persistence.contract.table.StateContract;
import com.ufla.lfapp.persistence.contract.table.SymbolContract;
import com.ufla.lfapp.persistence.contract.table.TransitionContract;
import com.ufla.lfapp.persistence.contract.view.MachineAlphabetViewContract;
import com.ufla.lfapp.persistence.contract.view.MachineFinalStateViewContract;
import com.ufla.lfapp.persistence.contract.view.MachineStatePositionViewContract;
import com.ufla.lfapp.persistence.contract.view.MachineStateViewContract;
import com.ufla.lfapp.persistence.contract.view.MachineTransitionPartialViewContract;
import com.ufla.lfapp.persistence.contract.view.MachineTransitionViewContract;

/**
 * Created by carlos on 03/08/16.
 */
public class FeedReaderDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "LFAppDB.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String PRIMARY_KEY = " PRIMARY KEY";
    private static final String AUTO_INCREMENT = " AUTOINCREMENT";
    private static final String COMMA_SEP = ",";
    private static final String END_LINE = "\n";
    private static final String CREATE_TABLE = "CREATE TABLE ";
    private static final String SEMICOLON = ";";

    private static final String SQL_CREATE_ENTRIES =
        CREATE_TABLE + GrammarContract.Columns.TABLE_GRAMMAR + " (" +
            GrammarContract.Columns.COLUMN_GRAMMAR_ID + INTEGER_TYPE + PRIMARY_KEY
                + AUTO_INCREMENT + COMMA_SEP + END_LINE +
            GrammarContract.Columns.COLUMN_GRAMMAR_GRAMMAR + TEXT_TYPE +
                ")" + SEMICOLON + END_LINE;

    private static final String SQL_CREATE_ENTRIES_2[] = new String[] {
        StateContract.CREATE_TABLE, StateContract.CREATE_INDEX, SymbolContract.CREATE_TABLE,
        SymbolContract.CREATE_INDEX, TransitionContract.CREATE_TABLE,
        TransitionContract.CREATE_INDEX, MachineContract.CREATE_TABLE,
        GridPositionContract.CREATE_TABLE, GridPositionContract.CREATE_INDEX,
        MachineFinalStateContract.CREATE_TABLE, MachineStatePositionContract.CREATE_TABLE,
        MachineTransitionContract.CREATE_TABLE, MachineAlphabetViewContract.CREATE_VIEW,
        MachineFinalStateViewContract.CREATE_VIEW, MachineStatePositionViewContract.CREATE_VIEW,
        MachineStateViewContract.CREATE_VIEW, MachineTransitionPartialViewContract.CREATE_VIEW,
                MachineTransitionViewContract.CREATE_VIEW };


    private static final String SQL_DELETE_ENTRIES[] = new String[]{
            "DROP TABLE IF EXISTS " + GrammarContract.Columns.TABLE_GRAMMAR,
            "DROP TABLE IF EXISTS " + MachineDotLanguageContract.TABLE_NAME };

    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(MachineDotLanguageContract.CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
//        for (String sqlDelete : SQL_DELETE_ENTRIES) {
//            db.execSQL(sqlDelete);
//        }
//        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void cleanHistory() {
        onUpgrade(this.getWritableDatabase(), DATABASE_VERSION, DATABASE_VERSION+1);
    }
}
