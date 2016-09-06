package com.ufla.lfapp.vo;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.*;
import org.junit.Test;

public class Grammar11Test {

    private Grammar g;

	/*
    S -> U | V | S
    U -> XaU | XaX | UaX
    V -> XbV | XbX | VbX
    X -> aXbX | bXaX | λ | A
    A -> aA | B
    B -> bB | A
    C -> CD | a
    D -> DC | b
	*/

    @Before
    public void setUp() {
        String[] variables = new String[]{"S", "A", "B", "C", "D", "U", "V", "T"};
        String[] terminals = new String[]{"a", "b"};
        String initialSymbol = "S";
        String[] rules = new String[]{
                "S -> U | V | S",
                "U -> TaU | TaT | UaT",
                "V -> TbV | TbT | VbT",
                "T -> aTbT | bTaT | λ | A",
                "A -> aA | B",
                "B -> bB | A",
                "C -> CD | a",
                "D -> DC | b" };

        this.g = new Grammar(variables, terminals, initialSymbol, rules);
    }

    @Test
    public void testInitialSymbolNotRecursive() {

        Grammar newG = g.getGrammarWithInitialSymbolNotRecursive(this.g, new AcademicSupport());

        String[] expectedVariables = new String[]{"S'", "S", "A", "B", "C", "D", "U", "V", "T"};
        String[] expectedTerminals = new String[]{"a", "b"};
        String expectedInitialSymbol = "S'";
        String[] expectedRules = new String[]{
                "S' -> S",
                "S -> U | V | S",
                "U -> TaU | TaT | UaT",
                "V -> TbV | TbT | VbT",
                "T -> aTbT | bTaT | λ | A",
                "A -> aA | B",
                "B -> bB | A",
                "C -> CD | a",
                "D -> DC | b" };

        Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);

        assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());

        assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());

        assertEquals(expectedGrammar.getRules(), newG.getRules());

        assertEquals(expectedGrammar.getVariables(), newG.getVariables());
    }

    @Test
    public void testGrammarEssentiallyNonContracting() {

        Grammar newG = g.getGrammarEssentiallyNoncontracting(this.g, new AcademicSupport());

        String[] expectedVariables = new String[] {"S", "A", "B", "C", "D", "U", "V", "T"};
        String[] expectedTerminals = new String[] {"a", "b"};
        String expectedInitialSymbol = "S";
        String[] expectedRules = new String[] {
                "S -> U | V | S",
                "U -> TaU | TaT | UaT | aU | aT | Ta | a | Ua",
                "V -> TbV | TbT | VbT | bV | bT | Tb | b | Vb",
                "T -> aTbT | bTaT | A | abT | aTb | ab | baT | bTa | ba",
                "A -> aA | B",
                "B -> bB | A",
                "C -> CD | a",
                "D -> DC | b" };

        Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);

        assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());

        assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());

        assertEquals(expectedGrammar.getRules(), newG.getRules());

        assertEquals(expectedGrammar.getVariables(), newG.getVariables());
    }

    @Test
    public void testChainRules() {
        Grammar newG = g.getGrammarWithoutChainRules(this.g, new AcademicSupport());

        String[] expectedVariables = new String[] {"S", "A", "B", "C", "D", "U", "V", "T"};
        String[] expectedTerminals = new String[] {"a", "b"};
        String expectedInitialSymbol = "S";
        String[] expectedRules = new String[] {
                "S -> TaU | TaT | UaT | TbV | TbT | VbT",
                "U -> TaU | TaT | UaT",
                "V -> TbV | TbT | VbT",
                "T -> aTbT | bTaT | λ | aA | bB",
                "A -> aA | bB",
                "B -> bB | aA",
                "C -> CD | a",
                "D -> DC | b" };

        Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);

        assertEquals(expectedGrammar.getRules(), newG.getRules());

        assertEquals(newG.getInitialSymbol(), expectedGrammar.getInitialSymbol());

        assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());

        assertEquals(expectedGrammar.getVariables(), newG.getVariables());
    }

    @Test
    public void  testNoTerminals() {
        Grammar newG = g.getGrammarWithoutNoTerm(this.g, new AcademicSupport());

        String[] expectedVariables = new String[] {"S", "C", "D", "U", "V", "T"};
        String[] expectedTerminals = new String[] {"a", "b"};
        String expectedInitialSymbol = "S";
        String[] expectedRules = new String[] {
                "S -> U | V | S",
                "U -> TaU | TaT | UaT",
                "V -> TbV | TbT | VbT",
                "T -> aTbT | bTaT | λ ",
                "C -> CD | a",
                "D -> DC | b" };

        Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);

        assertEquals(expectedGrammar.getRules(), newG.getRules());

        assertEquals(newG.getInitialSymbol(), expectedGrammar.getInitialSymbol());

        assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());

        assertEquals(expectedGrammar.getVariables(), newG.getVariables());
    }

    @Test
    public void testNoReach() {
        Grammar newG = g.getGrammarWithoutNoReach(this.g, new AcademicSupport());

        String[] expectedVariables = new String[] {"S", "A", "B", "U", "V", "T"};
        String[] expectedTerminals = new String[] {"a", "b"};
        String expectedInitialSymbol = "S";
        String[] expectedRules = new String[] {
                "S -> U | V | S",
                "U -> TaU | TaT | UaT",
                "V -> TbV | TbT | VbT",
                "T -> aTbT | bTaT | λ | A",
                "A -> aA | B",
                "B -> bB | A",};

        Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);

        assertEquals(expectedGrammar.getRules(), newG.getRules());

        assertEquals(newG.getInitialSymbol(), expectedGrammar.getInitialSymbol());

        assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());

        assertEquals(expectedGrammar.getVariables(), newG.getVariables());
    }

    @Test
    public void testFNC() {
        Grammar newG = g.FNC(this.g, new AcademicSupport());

        String[] expectedVariables = new String[] {"S'", "U", "V", "T", "T1", "T2", "T3", "T4",
                "T5", "T7", "T9", "T10", "T11", "T18"};
        String[] expectedTerminals = new String[] {"a", "b"};
        String expectedInitialSymbol = "S'";
        String[] expectedRules = new String[] {
                "S' -> TT18 | TT10 | UT10 | T2U | T2T | TT2 | a | UT2 | TT7 | TT3 | VT3 | T1V | T1T | TT1 | b | VT1",
                "U -> TT18 | TT10 | UT10 | T2U | T2T | TT2 | a | UT2",
                "V -> TT7 | TT3 | VT3 | T1V | T1T | TT1 | b | VT1",
                "T -> T2T11 | T1T9 | T2T3 | T2T4 | T2T1 | T1T10 | T1T5 | T1T2",
                "T1 -> b",
                "T2 -> a",
                "T3 -> T1T",
                "T4 -> TT1",
                "T5 -> TT2",
                "T7 -> T1V",
                "T9 -> TT10",
                "T10 -> T2T",
                "T11 -> TT3",
                "T18 -> T2U" };
        Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);
        System.out.println(newG.toStringRulesMapVToU());
        System.out.println(expectedGrammar.toStringRulesMapVToU());
        assertEquals(expectedGrammar.getRules(), newG.getRules());

        assertEquals(expectedGrammar.getRules(), newG.getRules());

        assertEquals(newG.getInitialSymbol(), expectedGrammar.getInitialSymbol());

        assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());

        assertEquals(expectedGrammar.getVariables(), newG.getVariables());
    }

    @Test
    public void testFNG() {
        Grammar newG = g.FNG(g, new AcademicSupport());
        boolean fng = true;
        for (Rule element : newG.getRules()) {
            int counter = 0;
            if (!element.getLeftSide().equals(newG.getInitialSymbol()) && element.getRightSide().equals("")) {
                fng = false;
            } else {
                for (int i = 0; i < element.getRightSide().length() && fng; i++) {
                    if (Character.isLowerCase(element.getRightSide().charAt(i))) {
                        counter++;
                    }
                }
                if (counter > 1) {
                    fng = false;
                }
            }
        }

        assertEquals(true, fng);
        assertTrue(newG.isFNG());
    }

    @Test
    public void testFNG2() {
        String[] variables = new String[]{"S", "A", "B", "C"};
        String[] terminals = new String[]{"a", "b"};
        String initialSymbol = "S";
        String[] rules = new String[]{
                "S -> AB | λ",
                "A -> AB | CB | a",
                "B -> AB | b",
                "C -> AC | c" };

        this.g = new Grammar(variables, terminals, initialSymbol, rules);
        this.g = g.FNC(g, new AcademicSupport());
        //Grammar gc = g.removingLeftRecursion(g, new AcademicSupport(), new HashMap<String, String>());
        Grammar newG = g.FNG(g, new AcademicSupport());

        boolean fng = true;
        for (Rule element : newG.getRules()) {
            int counter = 0;
            if (!element.getLeftSide().equals(newG.getInitialSymbol()) && element.getRightSide().equals("")) {
                fng = false;
            } else {
                for (int i = 0; i < element.getRightSide().length() && fng; i++) {
                    if (Character.isLowerCase(element.getRightSide().charAt(i))) {
                        counter++;
                    }
                }
                if (counter > 1) {
                    fng = false;
                }
            }
        }
        assertEquals(true, fng);
        assertTrue(newG.isFNG());
    }

    @Test
    public void testCYKAccept1() {
        g = g.FNC(g, new AcademicSupport());
        Set<String>[][] matrix = Grammar.CYK(g, "a");

        assertNotNull(matrix);
        assertNotNull(matrix[0][0]);
        assertNotEquals("", matrix[0][0]);

        Set<String> topVariables = matrix[0][0];

        assertTrue(topVariables.contains(g.getInitialSymbol()));

    }

    @Test
    public void testCYKAccept2() {
        g = g.FNC(g, new AcademicSupport());
        Set<String>[][] matrix = Grammar.CYK(g, "b");

        assertNotNull(matrix);
        assertNotNull(matrix[0][0]);
        assertNotEquals("", matrix[0][0]);

        Set<String> topVariables = matrix[0][0];

        assertTrue(topVariables.contains(g.getInitialSymbol()));

    }

    @Test
    public void testCYKAccept3() {
        g = g.FNC(g, new AcademicSupport());
        Set<String>[][] matrix = Grammar.CYK(g, "aba");

        assertNotNull(matrix);
        assertNotNull(matrix[0][0]);
        assertNotEquals("", matrix[0][0]);

        Set<String> topVariables = matrix[0][0];

        assertTrue(topVariables.contains(g.getInitialSymbol()));

    }

    @Test
    public void testCYKAccept4() {
        g = g.FNC(g, new AcademicSupport());
        Set<String>[][] matrix = Grammar.CYK(g, "abbbbabbbb");

        assertNotNull(matrix);
        assertNotNull(matrix[0][0]);
        assertNotEquals("", matrix[0][0]);

        Set<String> topVariables = matrix[0][0];

        assertTrue(topVariables.contains(g.getInitialSymbol()));

    }

    @Test
    public void testCYKAccept5() {
        g = g.FNC(g, new AcademicSupport());
        Set<String>[][] matrix = Grammar.CYK(g, "ababaaabbabbabb");

        assertNotNull(matrix);
        assertNotNull(matrix[0][0]);
        assertNotEquals("", matrix[0][0]);

        Set<String> topVariables = matrix[0][0];

        assertTrue(topVariables.contains(g.getInitialSymbol()));

    }

    @Test
    public void testCYKReject1() {
        g = g.FNC(g, new AcademicSupport());
        Set<String>[][] matrix = Grammar.CYK(g, "ab");

        assertNotNull(matrix);
        assertNotNull(matrix[0][0]);
        assertNotEquals("", matrix[0][0]);

        Set<String> topVariables = matrix[0][0];

        assertFalse(topVariables.contains(g.getInitialSymbol()));

    }

    @Test
    public void testCYKReject2() {
        g = g.FNC(g, new AcademicSupport());
        Set<String>[][] matrix = Grammar.CYK(g, "abba");

        assertNotNull(matrix);
        assertNotNull(matrix[0][0]);
        assertNotEquals("", matrix[0][0]);

        Set<String> topVariables = matrix[0][0];

        assertFalse(topVariables.contains(g.getInitialSymbol()));

    }

    @Test
    public void testCYKReject3() {
        g = g.FNC(g, new AcademicSupport());
        Set<String>[][] matrix = Grammar.CYK(g, "aaabbabbab");

        assertNotNull(matrix);
        assertNotNull(matrix[0][0]);
        assertNotEquals("", matrix[0][0]);

        Set<String> topVariables = matrix[0][0];

        assertFalse(topVariables.contains(g.getInitialSymbol()));

    }

}
