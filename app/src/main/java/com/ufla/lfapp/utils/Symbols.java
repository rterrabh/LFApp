package com.ufla.lfapp.utils;

/**
 * Created by carlos on 2/1/17.
 */

public class Symbols {

    public static final String CHECK_MARK = "✓";
    public static final String CROSS_MARK = "❌";
    public static final String LAMBDA = "λ";
    public static final String TRANSACTION = "δ";
    public static final String EMPTY_SET = "∅";
    public static final String BELONGS_TO = "∈";
    public static final String NOT_BELONGS_TO = "∉";
    public static final String INITIAL_SYMBOL_GRAMMAR = "S";
    public static final String ARROW = "→";
    public static final String ARROW_USER = "->";
    public static final String PIPE = "|";


    public static String emptyIsLambda(String str) {
        return (str.isEmpty()) ? LAMBDA : str;
    }

}
