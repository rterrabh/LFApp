package com.ufla.lfapp.utils;

/**
 * Created by carlos on 20/07/17.
 */

public class HtmlTags {

    public static final String BOLD_OPEN = "<b>";
    public static final String BOLD_CLOSE = "</b>";
    public static final String BREAK_LINE = "<br>";
    public static final String SUB_OPEN = "<sub>";
    public static final String SUB_CLOSE = "</sub>";
    public static final String SMALL_OPEN = "<small>";
    public static final String SMALL_CLOSE = "</small>";
    private static final String FONT_COLOR_OPEN = "<font color =\"%s\">";
    public static final String FONT_CLOSE = "</font>";

    public static String FONT_COLOR_OPEN(String color) {
        return String.format(FONT_COLOR_OPEN, color);
    }
}
