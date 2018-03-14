package com.ufla.lfapp.core;

import android.util.Pair;

import com.ufla.lfapp.core.machine.tm.TMMove;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by carlos on 12/14/16.
 */

public class Teste {

    Thread t;
    int onOneClickCount;
    int onDoubleTapCount;

    public Teste() {
        t = getThread();
    }

    public Thread getThread() {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(250);
                    onOneClick();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }



    public boolean onOneClick() {
        onOneClickCount++;
        return true;
    }

    public boolean onDown() {
        if (t.isAlive()) {
            t.interrupt();
            onDoubleTap();
        } else {
            t  = getThread();
            t.start();
        }

        return true;
    }

    @Test
    public void onDoubleTap() {
        onDoubleTapCount++;
    }

    String str = "<b>#2</b> Se [q<sub><small>j</small></sub>, B] ∈ <i>δ\\'</i>(q<sub><small>i</small></sub>, x, A), então\\t(A e B ∈ Γ U {λ})\\n\n" +
            "\\t<b><bullet></b><q<sub><small>i</small></sub>, A, <cb:#786DBE>q<sub><small>k</small></sub></cb>> → x<q<sub><small>j</small></sub>, B, <cb:#786DBE>q<sub><small>k</small></sub></colorBack>>, para todo <cb:#786DBE>q<sub><small>k</small></sub></colorBack> ∈ Q\\n\n" +
            "<b>#3</b> Se [q<sub><small>j</small></sub>, BA] ∈ <i>δ\\'</i>(q<sub><small>i</small></sub>, x, A), então\\t(A e B ∈ Γ)\\n\n" +
            "\\t<b><bullet></b> <qi, A, <cb:#786DBE>q<sub><small>k</small></sub></colorBack>> → x<q<sub><small>j</small></sub>, B, q<sub><small>n</small></sub>><<cb:#8FD45A>q<sub><small>n</small></sub></cb>, A, <cb:#786DBE>q<sub><small>k</small></sub></cb>>,<cb:#786DBE>q<sub><small>k</small></sub></cb>, <cb:#8FD45A>q<sub><small>n</small></sub></colorBack> ∈ Q\\n\n";


    private String[] arrayTrim(String[] array) {
        int n = array.length;
        String arrayTrim[] = new String[n];
        for (int i = 0; i < n; i++) {
            arrayTrim[i] = array[i].trim();
        }
        return arrayTrim;
    }


    private TMMove[] getMoves(String transition) {
        int begin = transition.indexOf(']', 0) + 1;
        begin = transition.indexOf(']', begin) + 1;
        begin = transition.indexOf('[', begin) + 1;
        int end = transition.indexOf(']', begin);
        String[] movesStr = arrayTrim(transition.substring(begin, end).split(","));
        int n = movesStr.length;
        TMMove[] moves = new TMMove[n];
        for (int i = 0; i < n; i++) {
            moves[i] = TMMove.getInstance(movesStr[i]);
        }
        return moves;
    }

    private String[] getTransitionsMultiTape(String label) {
        List<String> labels = new ArrayList<>();
        int begin = 0;
        int index = 0;
        while ((index = label.indexOf('/', index)) != -1) {
            index = label.indexOf(']', index);
            if (index == -1) {
                return null;
            }
            index = label.indexOf(']', index + 1);
            if (index == -1) {
                return null;
            }
            index++;
            labels.add(label.substring(begin, index).trim());
            begin = label.indexOf('[', index);
        }
        String[] labelsArrays = new String[labels.size()];
        return labels.toArray(labelsArrays);
    }

    @Test
    public void test2() {
        String a = "a ol/as";
        System.out.println(Arrays.deepToString(a.split("[ /]")));
//        System.out.println(Arrays.deepToString(getTransitionsMultiTape("[B, B]/[B, B] [S, S]")));
//        System.out.println(Arrays.deepToString(getMoves("[B, B]/[B, B] [S, S]")));
        String test = "a, b, d, a";
        String[] tokens = test.split(",");
        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = tokens[i].trim();
        }
        for (String to : tokens) {
            System.out.println("'" + to + "'");
        }

//        String a = "aaaa";
//        System.out.println(a.indexOf('a', 0));
//        String[] t = { "oi", "bom", "mesmo" };
//        System.out.println(Arrays.deepToString(t));
//        System.out.println(Arrays.toString(t));
//        System.out.println(t);
    }





}
