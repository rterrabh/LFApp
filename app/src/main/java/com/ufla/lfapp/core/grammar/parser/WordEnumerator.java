package com.ufla.lfapp.core.grammar.parser;

import com.ufla.lfapp.core.grammar.Grammar;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by carlos on 2/22/17.
 */

public class WordEnumerator {

    private Grammar grammar;
    private Set<String> alphabet;
    private int numberOfWords;
    private List<String> words;


    public WordEnumerator() {

    }

    public WordEnumerator(Grammar grammar, int numberOfWords) {
        this.grammar = grammar;
        this.numberOfWords = numberOfWords;
        this.words = new ArrayList<>(numberOfWords);
        alphabet = new TreeSet<>();
        setAlphabet(grammar.getTerminals());
        generateWords();
    }


    public void setAlphabet(Set<String> alphabet) {
        this.alphabet = alphabet;
    }

    public List<String> getWords() {
        return words;
    }

    private void generateWords() {
        int contWords = 0;
        int order = 1;
        words.add("");
        contWords++;
        while (contWords < numberOfWords) {
            Permut permut = new Permut(order, alphabet);
            Set<String> newWords = permut.getWords();
            words.addAll(newWords);
            contWords += newWords.size();
            order++;
        }
        while (contWords > numberOfWords) {
            int ind = words.size() - 1;
            words.remove(ind);
            contWords--;
        }
    }


}
