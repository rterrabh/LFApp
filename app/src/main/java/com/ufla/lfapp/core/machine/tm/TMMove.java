package com.ufla.lfapp.core.machine.tm;

import com.ufla.lfapp.R;
import com.ufla.lfapp.utils.ResourcesContext;

import java.io.Serializable;

/**
 * Created by carlos on 4/2/17.
 */

public enum TMMove implements Serializable {

    RIGHT,
    LEFT,
    STATIC;

    public static boolean test = false;
    private static String RIGHT_STR  = "R";
    private static String LEFT_STR  = "L";
    private static String STATIC_STR  = "S";

    static {
        if (!test) {
//            RIGHT_STR  = ResourcesContext.getString(R.string.direction_right);
//            LEFT_STR  = ResourcesContext.getString(R.string.direction_left);
//            STATIC_STR  = ResourcesContext.getString(R.string.direction_static);
        }
    }


    public static TMMove getInstance(String str) {
        if (str.toUpperCase().equals(RIGHT_STR)) {
            return RIGHT;
        }
        if (str.toUpperCase().equals(LEFT_STR)) {
            return LEFT;
        }
        if (str.toUpperCase().equals(STATIC_STR)) {
            return STATIC;
        }
        throw new RuntimeException(
                ResourcesContext.getString(R.string.exception_instance_not_found_for)
                        + str + "'!");
    }

    public String stringValue() {
        return toString();
    }

    @Override
    public String toString() {
        switch (this) {
            case RIGHT:
                return RIGHT_STR;
            case LEFT:
                return LEFT_STR;
            case STATIC:
                return STATIC_STR;
            default:
                throw new RuntimeException(ResourcesContext.getString(R.string.exception_instance_not_found));
        }
    }
}