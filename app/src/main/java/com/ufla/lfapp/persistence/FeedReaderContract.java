package com.ufla.lfapp.persistence;

import android.provider.BaseColumns;

/**
 * Created by root on 03/08/16.
 */
public final class FeedReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "grammar";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_GRAMMAR = "grammar";
        public static final String COLUMN_NAME_NULLABLE = null;
    }
}


