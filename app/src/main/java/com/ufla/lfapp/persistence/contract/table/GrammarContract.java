package com.ufla.lfapp.persistence.contract.table;

import android.provider.BaseColumns;

/**
 * Created by carlos on 03/08/16.
 */
public final class GrammarContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private GrammarContract() {}

    /* Inner class that defines the table contents */
    public static abstract class Columns implements BaseColumns {
        public static final String TABLE_GRAMMAR = "grammar";
        public static final String COLUMN_GRAMMAR_ID = "id";
        public static final String COLUMN_GRAMMAR_GRAMMAR = "grammar";
        public static final String COLUMN_GRAMMAR_NULLABLE = null;
    }
}


