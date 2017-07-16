package com.ufla.lfapp.utils;

/**
 * Created by root on 22/07/16.
 */
public enum Algorithm {

    NONE(0),
    CHOMSKY_NORMAL_FORM(1),
    GREIBACH_NORMAL_FORM(2),
    REMOVE_LEFT_RECURSION(3);

    private int value;

    Algorithm(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Algorithm getAlgorithm(int value) {
        switch (value) {
            case 0: return NONE;
            case 1: return CHOMSKY_NORMAL_FORM;
            case 2: return GREIBACH_NORMAL_FORM;
            case 3: return REMOVE_LEFT_RECURSION;
            default: return null;
        }
    }
}
