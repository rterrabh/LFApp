package com.ufla.lfapp.core.grammar.parser;

import com.ufla.lfapp.core.grammar.Grammar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by carlos on 2/22/17.
 */

public class AmbiguityVerification {

    private Grammar grammar;
    private int wordsTest;
    private List<String> words;
    private List<String> wordsAmbiguity;
    private List<Future<?>> futures;
    private final int NUMBER_OF_THREADS = 10;
    private ExecutorService executorService;

    public AmbiguityVerification(Grammar grammar, int wordsTest) {
        this.grammar = grammar;
        this.wordsTest = wordsTest;
        WordEnumerator wordEnumerator = new WordEnumerator(grammar, wordsTest);
        words = wordEnumerator.getWords();
        wordsAmbiguity = new CopyOnWriteArrayList<>();
        futures = new ArrayList<>();
        executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        verify();
    }

//    private void verifyLinear() {
//        for (final String word : words) {
//            System.out.println("Word -> " + word);
//            TreeDerivationParser treeDerivationParser = new TreeDerivationParser(grammar, word);
//            treeDerivationParser.parser();
//            if (treeDerivationParser.getTreeDerivationAux() != null) {
//                wordsAmbiguity.add(word);
//            }
//        }
//    }

    private void verify() {
        for (final String word : words) {
            futures.add(executorService.submit(new Runnable() {
                @Override
                public void run() {
                    TreeDerivationParser treeDerivationParser = new TreeDerivationParser(grammar, word);
                    treeDerivationParser.parser();
                    if (treeDerivationParser.getTreeDerivationAux() != null) {
                        wordsAmbiguity.add(word);
                    }
                }
            }));

        }
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
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
