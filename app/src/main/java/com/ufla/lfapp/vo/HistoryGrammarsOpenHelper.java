package com.ufla.lfapp.vo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by root on 15/07/16.
 */
//public class HistoryGrammarsOpenHelper extends SQLiteOpenHelper {

//    private static final int DATABASE_VERSION = 1;
//    private static final String DICTIONARY_TABLE_NAME = "grammar";
//    private static final String DICTIONARY_TABLE_CREATE =
//            "CREATE TABLE " + DICTIONARY_TABLE_NAME + " (" +
//                    KEY_WORD + " TEXT, " +
//                    KEY_DEFINITION + " TEXT);";
//
//    HistoryGrammarsOpenHelper(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(DICTIONARY_TABLE_CREATE);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        DATABASE_VERSION = newVersion
//    }
//}