package com.ufla.lfapp.core.grammar;

import com.ufla.lfapp.core.grammar.parser.AmbiguityVerification;
import com.ufla.lfapp.core.grammar.parser.RuleCompForLeftDerivation;
import com.ufla.lfapp.core.grammar.parser.WordEnumerator;
import com.ufla.lfapp.utils.ResourcesContext;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        AmbiguityVerification ambiguityVerification = new AmbiguityVerification(grammar, 6);
        Set<String> expected = new HashSet<>(Arrays.asList("a", "b", "c", "aa", "ab"));
        assertTrue(ambiguityVerification.isAmbiguityGrammar());
        assertEquals(expected, new HashSet<>(ambiguityVerification.getWordsAmbiguity()));
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
