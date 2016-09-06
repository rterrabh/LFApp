package com.ufla.lfapp.activities.utils;

/**
 * Created by root on 22/07/16.
 */
public enum Algorithm {

    NONE(0),
    CHOMSKY_NORMAL_FORM(1),
    GREIBACH_NORMAL_FORM(2);

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
            default: return null;
        }
    }
}
