package com.ufla.lfapp.core.grammar.parser;

import com.ufla.lfapp.core.grammar.Grammar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by carlos on 2/22/17.
 */

public class AmbiguityVerification {

    private Grammar grammar;
    private int wordsTest;
    private List<String> words;
    private List<String> wordsAmbiguity;

    public AmbiguityVerification(Grammar grammar, int wordsTest) {
        this.grammar = grammar;
        this.wordsTest = wordsTest;
        WordEnumerator wordEnumerator = new WordEnumerator(grammar, wordsTest);
        words = wordEnumerator.getWords();
        wordsAmbiguity = new ArrayList<>();
        verify();
    }

    private void verify() {
        for (String word : words) {
            TreeDerivationParser treeDerivationParser = new TreeDerivationParser(grammar, word);
            treeDerivationParser.parser();
            if (treeDerivationParser.getTreeDerivationAux() != null) {
                wordsAmbiguity.add(word);
            }
        }
    }

    public boolean isAmbiguityGrammar() {
        return !wordsAmbiguity.isEmpty();
    }
    
    public List<String> getWordsAmbiguity() {
        return wordsAmbiguity;
    }

}
