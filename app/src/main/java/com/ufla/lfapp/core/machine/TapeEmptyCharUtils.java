package com.ufla.lfapp.core.machine;

import com.ufla.lfapp.R;
import com.ufla.lfapp.utils.ResourcesContext;

import java.util.Arrays;

/**
 * Created by carlos on 4/24/17.
 */

public class TapeEmptyCharUtils {

    ///*
    public static final String EMPTY_CHAR_STR = "B";
    //*/
    /*
    public static final String EMPTY_CHAR_STR = ResourcesContext
            .getString(R.string.empty_char_tape);
    */
    public static final char EMPTY_CHAR = EMPTY_CHAR_STR.charAt(0);
    private static final int EMPTY_CHAR_INCREASE = 5;


    private TapeEmptyCharUtils() {

    }

    public static String increaseEmptyString() {
        char[] emptyString = new char[EMPTY_CHAR_INCREASE];
        for (int i = 0; i < EMPTY_CHAR_INCREASE; i++) {
            emptyString[i] = EMPTY_CHAR;
        }
        return new String(emptyString);
    }

    public static String getEmptyString(int lenght) {
        char[] emptyString = new char[lenght];
        Arrays.fill(emptyString, TapeEmptyCharUtils.EMPTY_CHAR);
        return new String(emptyString);
    }

    public static String[] getArrayEmptyString(int lenght) {
        String emptyString = Character.toString(EMPTY_CHAR);
        String[] emptyStrings = new String[lenght];
        Arrays.fill(emptyStrings, emptyString);
        return emptyStrings;
    }


}
