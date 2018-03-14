package com.ufla.lfapp.utils;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.BulletSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Created by carlos on 3/28/17.
 */

public class UtilSpannableTags {

    private static final String BULLET = "ullet>";
    public static final float SMALL = 0.65f;

    public static Object decodeSpan(String str, int index) {
        int length = str.length();
        if (length <= index + 1) {
            return null;
        }
        index++;
        switch(str.charAt(index-1)) {
            case 'b':
                if (str.charAt(index) == '>') {
                    return new StyleSpan(Typeface.BOLD);
                }
                if (length >= index + BULLET.length()
                        && str.substring(index, index + BULLET.length()).equals(BULLET)) {
                    return new BulletSpan();
                }
                return null;
            case 'i':
                if (str.charAt(index) == '>') {
                    return new StyleSpan(Typeface.ITALIC);
                }
                return null;
            case 's':
                if (str.charAt(index) == 'u'
                        && length >= index + 3
                        && str.substring(index+1, index + 3).equals("b>")) {
                    return new SubscriptSpan();
                }
                if (str.charAt(index) == 'm'
                        && length >= index + 5
                        && str.substring(index+1, index + 5).equals("all>")) {
                    return new RelativeSizeSpan(SMALL);
                }
                return null;
            case 'c':
                if (str.charAt(index) == 'b'
                        && length >= index + 1
                        && str.charAt(index+1) == ':') {
                    int finalIndex = str.indexOf('>', index);
                    return new BackgroundColorSpan(Color.parseColor(str
                            .substring(index+2, finalIndex)));
                }
                if (str.charAt(index) == 'b'
                        && length >= index + 1
                        && str.charAt(index+1) == '>') {
                    return new BackgroundColorSpan(0);
                }

        }
        return null;
    }

    static class SpanIndex {

        Object span;
        int initialIndex;

        public SpanIndex(Object span, int initialIndex) {
            this.span = span;
            this.initialIndex = initialIndex;
        }
    }

    public static Spannable decode(String str)
            throws Exception {
        SpannableStringBuilder spanSB = new SpannableStringBuilder();
        Deque<SpanIndex> spans = new ArrayDeque<>();
        int length = str.length();
        int index = 0;
        int indexSpan = str.indexOf('<');
        while (indexSpan != -1) {
            spanSB.append(str, index, indexSpan);
            if (length > indexSpan + 1
                    && str.charAt(indexSpan + 1) == '/') {
                Object spanClose = decodeSpan(str, indexSpan + 2);
                if (spanClose == null) {
                    spanSB.append(str.charAt(indexSpan));
                    index = indexSpan + 1;
                } else if (spans.isEmpty()) {
                    int indexFinal = str.indexOf('>', indexSpan);
                    throw new Exception("Span not open("
                            + str.substring(indexSpan, indexFinal + 1)
                            + ")!");
                } else {
                    SpanIndex span = spans.pop();
                    if (!span.span.getClass().equals(spanClose.getClass())) {
                        int indexFinal = str.indexOf('>', indexSpan);
                        throw new Exception("Span not open("
                                + str.substring(indexSpan, indexFinal + 1)
                                + ")!");
                    }
                    spanSB.setSpan(span.span, span.initialIndex, spanSB.length(),
                            Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    index = str.indexOf('>', indexSpan) + 1;
                }
            } else {
                Object span = decodeSpan(str, indexSpan + 1);
                if (span != null) {
                    if (span instanceof BulletSpan) {
                        spanSB.append(' ');
                        spanSB.setSpan(span, spanSB.length() - 1, spanSB.length(),
                                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    } else {
                        spans.push(new SpanIndex(span, spanSB.length()));
                    }
                    index = str.indexOf('>', indexSpan) + 1;
                } else {
                    spanSB.append(str.charAt(indexSpan));
                    index = indexSpan + 1;
                }
            }
            indexSpan = str.indexOf('<', index);
        }
        spanSB.append(str, index, str.length());
        return spanSB;
    }
}
