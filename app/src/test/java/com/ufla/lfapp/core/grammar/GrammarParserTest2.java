package com.ufla.lfapp.core.grammar;

import com.ufla.lfapp.R;
import com.ufla.lfapp.utils.ResourcesContext;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by carlos on 10/21/17.
 */

public class GrammarParserTest2 {

    @BeforeClass
    public static void setTest() {
        ResourcesContext.isTest = true;
    }

    @Test
    public void testVerifyInput() {
        assertTrue(GrammarParser.verifyInputGrammar("S -> a | A\nA -> b"));
    }

    @Test
    public void testInputValidate1() {
        assertTrue(GrammarParser.inputValidate("S -> a | A\nA -> b | B12\nB12 -> b", new StringBuilder()));
    }

    @Test
    public void testInputValidate2() {
        assertFalse(GrammarParser.inputValidate("S -> a | A", new StringBuilder()));
    }

    @Test
    public void testContainsSentence1() {
        Grammar grammar = new Grammar("S -> a | A\nA -> b | B12\nB12 -> b");
        String sentence = "B12";
        assertTrue(GrammarParser.containsSentence(grammar, sentence));
    }

    @Test
    public void testContainsSentence2() {
        GrammarParser grammarParser = new GrammarParser();
        Grammar grammar = new Grammar("S -> a | A\nA -> b | B12\nB12 -> b");
        String sentence = "B1";
        assertFalse(GrammarParser.containsSentence(grammar, sentence));
    }

    @Test
    public void testClassifiesGrammar1() {
        String expectedClass = ResourcesContext.getString(R.string.is_regular_grammar);
        Grammar grammar = new Grammar("S -> a | b");
        assertEquals(expectedClass, GrammarParser.classifiesGrammar(grammar, new StringBuilder()));
    }

    @Test
    public void testClassifiesGrammar2() {
        String expectedClass = ResourcesContext.getString(R.string.is_context_free_grammar);
        Grammar grammar = new Grammar("S -> aSb");
        assertEquals(expectedClass, GrammarParser.classifiesGrammar(grammar, new StringBuilder()));
    }

    @Test
    public void testClassifiesGrammar3() {
        String expectedClass = ResourcesContext.getString(R.string.is_sensible_context_grammar);
        Grammar grammar = new Grammar("AS -> SA");
        assertEquals(expectedClass, GrammarParser.classifiesGrammar(grammar, new StringBuilder()));
    }

    @Test
    public void testClassifiesGrammar4() {
        String expectedClass = ResourcesContext.getString(R.string.is_unrestricted_grammar);
        Grammar grammar = new Grammar("AS -> AS | a");
        assertEquals(expectedClass, GrammarParser.classifiesGrammar(grammar, new StringBuilder()));
    }

    @Test
    public void testClassifiesGrammar5() {
        String expectedClass = ResourcesContext.getString(R.string.not_class_grammar_found);
        Grammar grammar = new Grammar("AS -> AS | a\n" + Grammar.LAMBDA + " -> a");
        assertEquals(expectedClass, GrammarParser.classifiesGrammar(grammar, new StringBuilder()));
    }

    @Test
    public void testChecksGrammar1() {
        Grammar grammar = new Grammar("S -> a | A\nA -> a | " + Grammar.LAMBDA);
        assertFalse(GrammarParser.checksGrammar(grammar, new AcademicSupport()));
    }

    @Test
    public void testChecksGrammar2() {
        Grammar grammar = new Grammar("S -> a | S");
        assertFalse(GrammarParser.checksGrammar(grammar, new AcademicSupport()));
    }

    @Test
    public void testChecksGrammar3() {
        Grammar grammar = new Grammar("S -> ab | ba");
        assertTrue(GrammarParser.checksGrammar(grammar, new AcademicSupport()));
    }

    @Test
    public void testExistsRecursion1() {
        Grammar grammar = new Grammar("S -> ab | ba");
        Map<String, String> variablesInOrder = new LinkedHashMap<>();
        variablesInOrder.put("1", "S");
        Set<String> olderVariables = new LinkedHashSet<>();
        olderVariables.add("S");
        assertFalse(GrammarParser.existsRecursion(grammar, variablesInOrder, olderVariables));
    }

    @Test
    public void testExistsRecursion2() {
        Grammar grammar = new Grammar("S -> ab | ba | S");
        Map<String, String> variablesInOrder = new LinkedHashMap<>();
        variablesInOrder.put("S", "1");
        Set<String> olderVariables = new LinkedHashSet<>();
        olderVariables.add("S");
        assertTrue(GrammarParser.existsRecursion(grammar, variablesInOrder, olderVariables));
    }

    @Test
    public void testGetsFirstCharacter1() {
        String rightSide = "A12";
        String expected = "A12";
        assertEquals(expected, GrammarParser.getsFirstCharacter(rightSide));
    }

    @Test
    public void testGetsFirstCharacter2() {
        String rightSide = "A12b";
        String expected = "A12";
        assertEquals(expected, GrammarParser.getsFirstCharacter(rightSide));
    }

    @Test
    public void testGetsFirstCharacter3() {
        String rightSide = "aAB";
        String expected = "a";
        assertEquals(expected, GrammarParser.getsFirstCharacter(rightSide));
    }

    @Test
    public void testTurnsTreesetOnArray1() {
        String[] expectedVariables = new String[]{"S", "A", "B", "C", "V"};
        String[] expectedTerminals = new String[]{"a", "b"};
        String expectedInitialSymbol = "S";
        String[] expectedRules = new String[]{
                "S -> AB | BC | BV",
                "A -> BA | a",
                "B -> VV | VC | CV | CC | b",
                "C -> AB",
                "V -> a"};
        Grammar grammar = new Grammar(expectedVariables, expectedTerminals,
                expectedInitialSymbol, expectedRules);
        Set<String>[][] matrix = Grammar.CYK(grammar, "bbabaa");
        String[][] matrixStrExpected = {{"{A, S}", "", "", "", "", ""},
                {"{B}", "{A, S}", "", "", "", ""},
                {"{S, C}", "{B}", "{A, S}", "", "", ""},
                {"{A}", "{S, C}", "{B}", "-", "", ""},
                {"-", "{A, S}", "{S, C}", "{A, S}", "{B}", ""},
                {"{B}", "{B}", "{A, V}", "{B}", "{A, V}", "{A, V}"},
                {"b", "b", "a", "b", "a", "a"}};
        assertArrayEquals(matrixStrExpected, GrammarParser.turnsTreesetOnArray(matrix, "bbabaa"));
    }


}
