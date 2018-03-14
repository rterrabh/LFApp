package com.ufla.lfapp.core.grammar;

import com.ufla.lfapp.core.grammar.parser.AmbiguityVerification;
import com.ufla.lfapp.core.grammar.parser.NodeDerivationParser;
import com.ufla.lfapp.core.grammar.parser.RuleCompForLeftDerivation;
import com.ufla.lfapp.core.grammar.parser.TreeDerivationParser;
import com.ufla.lfapp.core.grammar.parser.WordEnumerator;
import com.ufla.lfapp.utils.MyConsumer;
import com.ufla.lfapp.utils.ResourcesContext;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by carlos on 10/21/17.
 */

public class AmbiguityVerificationTest {

    @BeforeClass
    public static void setTest() {
        ResourcesContext.isTest = true;
    }


    @Test
    public void testAmbiguityVerification() {
        Grammar grammar = new Grammar("X -> XaX | XbX | c | X | A | " + Grammar.LAMBDA + "\n A -> X");
        AmbiguityVerification ambiguityVerification = new AmbiguityVerification(grammar, 100);
        assertTrue(ambiguityVerification.isAmbiguityGrammar());
//        Set<String> expected = new HashSet<>(Arrays.asList("a", "b", "c", "aa", "ab"));
//        assertEquals(expected, new HashSet<>(ambiguityVerification.getWordsAmbiguity()));
    }

    @Test
    public void testAmbiguityVerification2() {
        final Grammar grammar = new Grammar("X -> XaX | XbX | X | c");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                AmbiguityVerification ambiguityVerification = new AmbiguityVerification(grammar, 200);
            }
        });
        thread.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.interrupt();
    }

    @Test
    public void testNewAmbiguityVerification() throws InterruptedException {
        Grammar grammar = new Grammar("X -> XaX | XbX | c | X | A | " + Grammar.LAMBDA + "\n A -> X");
        final AtomicBoolean callback = new AtomicBoolean(false);
        TreeDerivationParser treeDerivationParser = new TreeDerivationParser(grammar);
        treeDerivationParser.checkAmbiguity(new MyConsumer<TreeDerivationParser>() {
            @Override
            public void accept(TreeDerivationParser treeDerivationParser) {
                System.out.println("teste");
                assertTrue(treeDerivationParser.isAmbiguity());
                System.out.println("Ambiguidade na palavra: '" + treeDerivationParser.getWord() + "'");
                System.out.println("Derivação 1: " + treeDerivationParser.getTreeDerivation().
                        getDerivation());
                System.out.println("Derivação 2: " + treeDerivationParser.getTreeDerivationAux().
                        getDerivation());
                System.out.println("-----------------------------------------");
                callback.set(true);
            }
        }, null);
        while (!callback.get()) {
            Thread.sleep(500);
        }
    }

    @Test
    public void test10() {
        SortedMap<Integer, String> nodes = new TreeMap<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }
        });
        nodes.put(10, "ao");
        nodes.put(5, "aa");
        nodes.put(8, "a2");
        nodes.put(4, "a4");
        List<Integer> modified = new ArrayList<>();
        for (SortedMap.Entry<Integer, String> nodesEntry : nodes.entrySet()) {
            int key = nodesEntry.getKey();
            if (key > 5) {
                modified.add(key);
            } else {
                break;
            }

        }
        for (Integer key : modified) {
            String str = nodes.remove(key);
            nodes.put(key + 3, str);
        }
        for (SortedMap.Entry<Integer, String> nodesEntry : nodes.entrySet()) {
            System.out.println(nodesEntry);
        }
    }

    @Test
    public void testNewAmbiguityVerification2() throws InterruptedException {
        Grammar grammar = new Grammar("X -> XaX | XbX | X | c");
        final AtomicBoolean callback = new AtomicBoolean(false);
        TreeDerivationParser treeDerivationParser = new TreeDerivationParser(grammar);
        treeDerivationParser.checkAmbiguity(new MyConsumer<TreeDerivationParser>() {
            @Override
            public void accept(TreeDerivationParser treeDerivationParser) {
                assertTrue(treeDerivationParser.isAmbiguity());
                System.out.println("Ambiguidade na palavra: '" + treeDerivationParser.getWord() + "'");
                System.out.println("Derivação 1: " + treeDerivationParser.getTreeDerivation().
                        getDerivation());
                System.out.println("Derivação 2: " + treeDerivationParser.getTreeDerivationAux().
                        getDerivation());
                System.out.println("-----------------------------------------");
                callback.set(true);
            }
        }, null);
        while (!callback.get()) {
            Thread.sleep(500);
        }
    }

    @Test
    public void testNewAmbiguityVerification3() throws InterruptedException {
        Grammar grammar = new Grammar("S -> aSb | aSbb | " + Grammar.LAMBDA);
        final AtomicBoolean callback = new AtomicBoolean(false);
        TreeDerivationParser treeDerivationParser = new TreeDerivationParser(grammar);
        treeDerivationParser.checkAmbiguity(new MyConsumer<TreeDerivationParser>() {
            @Override
            public void accept(TreeDerivationParser treeDerivationParser) {
                assertTrue(treeDerivationParser.isAmbiguity());
                System.out.println("Ambiguidade na palavra: '" + treeDerivationParser.getWord() + "'");
                System.out.println("Derivação 1: " + treeDerivationParser.getTreeDerivation().
                        getDerivation());
                System.out.println("Derivação 2: " + treeDerivationParser.getTreeDerivationAux().
                        getDerivation());
                System.out.println("-----------------------------------------");
                callback.set(true);
            }
        }, null);
        while (!callback.get()) {
            Thread.sleep(500);
        }
    }

    @Test
    public void testNewAmbiguityVerification4() throws InterruptedException {
        Grammar grammar = new Grammar("S -> aSb | A | " + Grammar.LAMBDA + "\nA -> aAbb | abb");
        final AtomicBoolean callback = new AtomicBoolean(false);
        TreeDerivationParser treeDerivationParser = new TreeDerivationParser(grammar);
        treeDerivationParser.checkAmbiguity(new MyConsumer<TreeDerivationParser>() {
            @Override
            public void accept(TreeDerivationParser treeDerivationParser) {
                assertFalse(treeDerivationParser.isAmbiguity());
                callback.set(true);
            }
        }, null);
        while (!callback.get()) {
            Thread.sleep(500);
        }
    }

    @Test
    public void testWordEnumerador() {
        final Grammar grammar = new Grammar("X -> XaX | XbX | X | c | " + Grammar.LAMBDA);
        new WordEnumerator();
        WordEnumerator wordEnumerator = new WordEnumerator(grammar, 5);
        List<String> expected = Arrays.asList("", "a", "b", "c", "aa");
        assertEquals(expected, wordEnumerator.getWords());
    }

    @Test
    public void testRuleComp1() {
        Rule rule1 = new Rule("S", "Aa");
        Rule rule2 = new Rule("S", "Da");
        RuleCompForLeftDerivation ruleCompForLeftDerivation = new RuleCompForLeftDerivation();
        assertTrue(ruleCompForLeftDerivation.compare(rule1, rule2) < 0);
    }

    @Test
    public void testRuleComp3() {
        Rule rule1 = new Rule("S", "Sa");
        Rule rule2 = new Rule("S", "Da");
        RuleCompForLeftDerivation ruleCompForLeftDerivation = new RuleCompForLeftDerivation();
        assertTrue(ruleCompForLeftDerivation.compare(rule1, rule2) > 0);
    }

    @Test
    public void testRuleComp2() {
        Rule rule1 = new Rule("S", "Aa");
        Rule rule2 = new Rule("S", "Sa");
        RuleCompForLeftDerivation ruleCompForLeftDerivation = new RuleCompForLeftDerivation();
        assertEquals(-1, ruleCompForLeftDerivation.compare(rule1, rule2));
    }

}
