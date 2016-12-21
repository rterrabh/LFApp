package com.ufla.lfapp.vo.grammar;

/**
 * Created by lfapp on 06/09/16.
 */
public enum ChomskyClassification {

    REGULAR(3),
    CONTEXT_FREE(2),
    CONTEXT_SENSITIVE(1),
    RECURSIVELY_ENUMERABLE(0);

    private int value;

    ChomskyClassification(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ChomskyClassification getChomskyClassification(int value) {
        switch (value) {
            case 0: return RECURSIVELY_ENUMERABLE;
            case 1: return CONTEXT_SENSITIVE;
            case 2: return CONTEXT_FREE;
            case 3: return REGULAR;
            default: return null;
        }
    }
}
