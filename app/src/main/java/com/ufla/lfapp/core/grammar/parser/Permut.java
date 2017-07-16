package com.ufla.lfapp.core.grammar.parser;

import java.util.Set;
import java.util.TreeSet;

/**
 * Created by carlos on 2/26/17.
 */

public class Permut {

    private Set<String> symbols;
    private Set<String> words;
    private int order;

    public Permut(int order, Set<String> symbols) {
        this.order = order;
        this.symbols = symbols;
        words = new TreeSet<>();
        generateWords("", 0);
    }

    public Set<String> getWords() {
        return words;
    }


    private void generateWords(String word, int i) {
        if (i == order - 1) {
            for (String symbol : symbols) {
                words.add(word + symbol);
            }
        } else {
            for (String symbol : symbols) {
                generateWords(word + symbol, i + 1);
            }
        }
    }
}
