package com.ufla.lfapp.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ufla.lfapp.core.machine.MachineType;
import com.ufla.lfapp.core.machine.dotlang.DotLanguage;
import com.ufla.lfapp.core.machine.fsa.FiniteStateAutomatonGUI;
import com.ufla.lfapp.persistence.contract.table.MachineDotLanguageContract;

/**
 * Created by carlos on 15/07/17.
 */

public class UpgradeDBV1ToV2 {

    private SQLiteDatabase db;

    public UpgradeDBV1ToV2(SQLiteDatabase db) {
        this.db = db;
    }

    public void upgrade() {
        String tempMachineDotLang = "TEMP_" + MachineDotLanguageContract.TABLE_NAME;
        db.execSQL(" ALTER TABLE " + MachineDotLanguageContract.TABLE_NAME +
                " RENAME TO " + tempMachineDotLang + ";");
        System.out.println(MachineDotLanguageContract.CREATE_TABLE);
        db.execSQL(MachineDotLanguageContract.CREATE_TABLE);
        String[] projection = {
                MachineDotLanguageContract.Columns._ID,
                MachineDotLanguageContract.Columns.DOT_LANGUAGE,
                MachineDotLanguageContract.Columns.LABEL,
                MachineDotLanguageContract.Columns.CREATION_TIME,
                MachineDotLanguageContract.Columns.CONT_USES };
        Cursor c = db.query(
                tempMachineDotLang,                 // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                     // The sort order
        );
        int cont = 0;
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String graph = c.getString(c.getColumnIndex(
                            MachineDotLanguageContract.Columns.DOT_LANGUAGE));
                    DotLanguage dotLanguage = new DotLanguage(graph);
                    try {
                        FiniteStateAutomatonGUI fsaGUI = dotLanguage.toAutomatonGUI();
                        DotLanguage newDotLanguage = new DotLanguage(fsaGUI,
                                fsaGUI.getStateGridPositions());
                        cont++;
                        String label = c.getString(c.getColumnIndex(
                                MachineDotLanguageContract.Columns.LABEL));
                        String creationTime = c.getString(c.getColumnIndex(
                                MachineDotLanguageContract.Columns.CREATION_TIME));
                        long id = c.getLong(c.getColumnIndex(
                                MachineDotLanguageContract.Columns._ID));
                        int contUses = c.getInt(c.getColumnIndex(
                                MachineDotLanguageContract.Columns.CONT_USES));
                        ContentValues values = new ContentValues();
                        values.put(MachineDotLanguageContract.Columns.DOT_LANGUAGE, newDotLanguage.getGraph());
                        values.put(MachineDotLanguageContract.Columns.LABEL, label);
                        values.put(MachineDotLanguageContract.Columns.CREATION_TIME,
                                creationTime);
                        values.put(MachineDotLanguageContract.Columns._ID,
                                id);
                        values.put(MachineDotLanguageContract.Columns.CONT_USES,
                                contUses);
                        values.put(MachineDotLanguageContract.Columns.MACHINE_TYPE,
                                MachineType.FSA.ordinal());
                        db.insert(MachineDotLanguageContract.TABLE_NAME,
                                null,
                                values);
                    } catch (Exception e) {
                        // nothing to do
                    }
                } while (c.moveToNext());
            }
            c.close();
        }
        System.out.println(cont);
        db.execSQL("DROP TABLE " + tempMachineDotLang + ";");
    }

}
