package com.ufla.lfapp.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 03/08/16.
 */
public class DbAcess {

    private  FeedReaderDbHelper mDbHelper;

    public DbAcess(Context context) {
        mDbHelper = new FeedReaderDbHelper(context);
    }

    public void putGrammar(String grammar) {
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_GRAMMAR, grammar);

        // Insert the new row, returning the primary key value of the new row
       db.insert(FeedReaderContract.FeedEntry.TABLE_NAME,
                FeedReaderContract.FeedEntry.COLUMN_NAME_NULLABLE,
                values);
    }

    public Map<String, Integer> readGrammars() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                FeedReaderContract.FeedEntry.COLUMN_NAME_ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_GRAMMAR,
//                FeedReaderContract.FeedEntry.COLUMN_NAME_UPDATED
        };

        // How you want the results sorted in the resulting Cursor
//        String sortOrder =
//                FeedReaderContract.FeedEntry.COLUMN_NAME_UPDATED + " DESC";
        String selection = "";
        String[] selectionArgs = {""};

        Cursor c = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                     // The sort order
        );
        Map<String, Integer> result = new HashMap<>();
        if(c != null ) {
            if (c.moveToFirst()) {
                int columnIndexGrammar = c.getColumnIndex
                        (FeedReaderContract.FeedEntry.COLUMN_NAME_GRAMMAR);
                int columnIndexId = c.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_ID);
                do {
                    result.put(c.getString(columnIndexGrammar), c.getInt(columnIndexId));
                } while(c.moveToNext());
            }
        }
        c.close();
        return result;
    }

    public void deleteGrammar(Integer id) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Define 'where' part of query.
        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_ID
                + " = ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { String.valueOf(id) };
        // Issue SQL statement.
        db.delete(FeedReaderContract.FeedEntry.TABLE_NAME, selection, selectionArgs);
    }

    public void cleanHistory() {
        mDbHelper.cleanHistory();
    }


}
