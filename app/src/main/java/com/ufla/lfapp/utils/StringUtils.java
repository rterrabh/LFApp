package com.ufla.lfapp.utils;

/**
 * Created by carlos on 4/19/17.
 */

public class StringUtils {

    public static String[] toString(StringBuilder[] stringBuilders) {
        int n = stringBuilders.length;
        String[] strings = new String[n];
        for (int i = 0; i < n; i++) {
            strings[i] = stringBuilders[i].toString();
        }
        return strings;
    }

}
