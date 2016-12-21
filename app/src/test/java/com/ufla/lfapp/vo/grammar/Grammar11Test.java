package com.ufla.lfapp.vo.grammar;

import com.ufla.lfapp.vo.grammar.*;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import org.junit.*;
import org.junit.Test;

public class Grammar11Test {

    private Grammar g;

	/*
    S -> U | V | S
    U -> TaU | TaT | UaT
    V -> TbV | TbT | VbT
    T -> aTbT | bTaT | λ | A
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
        Grammar newG = this.g.getGrammarWithInitialSymbolNotRecursive(this.g, new AcademicSupport());

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
        Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals,
                expectedInitialSymbol, expectedRules);

        assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());
        assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
        assertEquals(expectedGrammar.getRules(), newG.getRules());
        assertEquals(expectedGrammar.getVariables(), newG.getVariables());
    }

    @Test
    public void testGrammarEssentiallyNonContracting() {
        Grammar newG = this.g.getGrammarEssentiallyNoncontracting(this.g, new AcademicSupport());

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
        Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals,
                expectedInitialSymbol, expectedRules);

        assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());
        assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
        assertEquals(expectedGrammar.getRules(), newG.getRules());
        assertEquals(expectedGrammar.getVariables(), newG.getVariables());
    }

    @Test
    public void testChainRules() {
        Grammar newG = this.g.getGrammarWithoutChainRules(this.g, new AcademicSupport());

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
        Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals,
                expectedInitialSymbol, expectedRules);

        assertEquals(expectedGrammar.getRules(), newG.getRules());
        assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());
        assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
        assertEquals(expectedGrammar.getVariables(), newG.getVariables());
    }

    @Test
    public void  testNoTerminals() {
        Grammar newG = this.g.getGrammarWithoutNoTerm(this.g, new AcademicSupport());

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
        Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals,
                expectedInitialSymbol, expectedRules);

        assertEquals(expectedGrammar.getRules(), newG.getRules());
        assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());
        assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
        assertEquals(expectedGrammar.getVariables(), newG.getVariables());
    }

    @Test
    public void testNoReach() {
        Grammar newG = this.g.getGrammarWithoutNoReach(this.g, new AcademicSupport());

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
        Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals,
                expectedInitialSymbol, expectedRules);

        assertEquals(expectedGrammar.getRules(), newG.getRules());
        assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());
        assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
        assertEquals(expectedGrammar.getVariables(), newG.getVariables());
    }

    @Test
    public void testPreFNC2() {
        Grammar newG = this.g.getGrammarWithInitialSymbolNotRecursive(this.g, new AcademicSupport());
        newG = newG.getGrammarEssentiallyNoncontracting(newG, new AcademicSupport());

        String[] expectedVariables = new String[] {"S'", "S", "A", "B", "C", "D", "U", "V", "T"};
        String[] expectedTerminals = new String[] {"a", "b"};
        String expectedInitialSymbol = "S'";
        String[] expectedRules = new String[] {
                "S' -> S",
                "S -> U | V | S",
                "U -> TaU | TaT | UaT | aU | aT | Ta | a | Ua",
                "V -> TbV | TbT | VbT | bV | bT | Tb | b | Vb",
                "T -> aTbT | bTaT | A | abT | aTb | ab | baT | bTa | ba",
                "A -> aA | B",
                "B -> bB | A",
                "C -> CD | a",
                "D -> DC | b" };
        Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals,
                expectedInitialSymbol, expectedRules);

        assertEquals(expectedGrammar.getRules(), newG.getRules());
        assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());
        assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
        assertEquals(expectedGrammar.getVariables(), newG.getVariables());
    }

    @Test
    public void testPreFNC3() {
        Grammar newG = this.g.getGrammarWithInitialSymbolNotRecursive(this.g, new AcademicSupport());
        newG = newG.getGrammarEssentiallyNoncontracting(newG, new AcademicSupport());
        newG = newG.getGrammarWithoutChainRules(newG, new AcademicSupport());

        String[] expectedVariables = new String[] {"S'", "S", "A", "B", "C", "D", "U", "V", "T"};
        String[] expectedTerminals = new String[] {"a", "b"};
        String expectedInitialSymbol = "S'";
        String[] expectedRules = new String[] {
                "S' -> TaU | TaT | UaT | aU | aT | Ta | a | Ua | TbV | TbT | VbT | bV | bT | " +
                        "Tb | b | Vb",
                "S -> TaU | TaT | UaT | aU | aT | Ta | a | Ua | TbV | TbT | VbT | bV | bT | " +
                        "Tb | b | Vb",
                "U -> TaU | TaT | UaT | aU | aT | Ta | a | Ua",
                "V -> TbV | TbT | VbT | bV | bT | Tb | b | Vb",
                "T -> aTbT | bTaT | aA | bB | abT | aTb | ab | baT | bTa | ba",
                "A -> aA | bB",
                "B -> bB | aA",
                "C -> CD | a",
                "D -> DC | b" };
        Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals,
                expectedInitialSymbol, expectedRules);

        assertEquals(expectedGrammar.getRules(), newG.getRules());
        assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());
        assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
        assertEquals(expectedGrammar.getVariables(), newG.getVariables());
    }

    @Test
    public void testPreFNC4() {
        Grammar newG = this.g.getGrammarWithInitialSymbolNotRecursive(this.g, new AcademicSupport());
        newG = newG.getGrammarEssentiallyNoncontracting(newG, new AcademicSupport());
        newG = newG.getGrammarWithoutChainRules(newG, new AcademicSupport());
        newG = newG.getGrammarWithoutNoTerm(newG, new AcademicSupport());

        String[] expectedVariables = new String[] {"S'", "S", "C", "D", "U", "V", "T"};
        String[] expectedTerminals = new String[] {"a", "b"};
        String expectedInitialSymbol = "S'";
        String[] expectedRules = new String[] {
                "S' -> TaU | TaT | UaT | aU | aT | Ta | a | Ua | TbV | TbT | VbT | bV | bT | " +
                        "Tb | b | Vb",
                "S -> TaU | TaT | UaT | aU | aT | Ta | a | Ua | TbV | TbT | VbT | bV | bT | " +
                        "Tb | b | Vb",
                "U -> TaU | TaT | UaT | aU | aT | Ta | a | Ua",
                "V -> TbV | TbT | VbT | bV | bT | Tb | b | Vb",
                "T -> aTbT | bTaT | abT | aTb | ab | baT | bTa | ba",
                "C -> CD | a",
                "D -> DC | b" };
        Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals,
                expectedInitialSymbol, expectedRules);

        assertEquals(expectedGrammar.getRules(), newG.getRules());
        assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());
        assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
        assertEquals(expectedGrammar.getVariables(), newG.getVariables());
    }

    @Test
    public void testPreFNCComplete() {
        Grammar newG = this.g.getGrammarWithInitialSymbolNotRecursive(this.g, new AcademicSupport());
        newG = newG.getGrammarEssentiallyNoncontracting(newG, new AcademicSupport());
        newG = newG.getGrammarWithoutChainRules(newG, new AcademicSupport());
        newG = newG.getGrammarWithoutNoTerm(newG, new AcademicSupport());
        newG = newG.getGrammarWithoutNoReach(newG, new AcademicSupport());

        String[] expectedVariables = new String[] {"S'", "U", "V", "T"};
        String[] expectedTerminals = new String[] {"a", "b"};
        String expectedInitialSymbol = "S'";
        String[] expectedRules = new String[] {
                "S' -> TaU | TaT | UaT | aU | aT | Ta | a | Ua | TbV | TbT | VbT | bV | bT | " +
                        "Tb | b | Vb",
                "U -> TaU | TaT | UaT | aU | aT | Ta | a | Ua",
                "V -> TbV | TbT | VbT | bV | bT | Tb | b | Vb",
                "T -> aTbT | bTaT | abT | aTb | ab | baT | bTa | ba" };
        Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals,
                expectedInitialSymbol, expectedRules);

        assertEquals(expectedGrammar.getRules(), newG.getRules());
        assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());
        assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
        assertEquals(expectedGrammar.getVariables(), newG.getVariables());
    }

    @Test
    public void testFNC() {
        Grammar newG = g.FNC(this.g, new AcademicSupport());

        String[] expectedVariables = new String[] {"S'", "U", "V", "T", "A'", "B'", "T1", "T2",
                "T3", "T4", "T5", "T6", "T7", "T8"};
        String[] expectedTerminals = new String[] {"a", "b"};
        String expectedInitialSymbol = "S'";
        String[] expectedRules = new String[] {
                "S' -> TT2 | TT1 | UT1 | A'U | A'T | A'T | TA' | a | UA' | TT4 | TT3 | VT3 | " +
                        "B'V | B'T | TB' | b | VB'",
                "U -> TT2 | TT1 | UT1 | A'U | A'T | TA' | a | UA'",
                "V -> TT4 | TT3 | VT3 | B'V | B'T | TB' | b | VB'",
                "T -> A'T6 | B'T8 | A'T3 | A'T5 | A'B' | B'T1 | B'T7 | B'A'",
                "A' -> a",
                "B' -> b",
                "T1 -> A'T",
                "T2 -> A'U",
                "T3 -> B'T",
                "T4 -> B'V",
                "T5 -> TB'",
                "T6 -> TT3",
                "T7 -> TA'",
                "T8 -> TT1" };
        Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals,
                expectedInitialSymbol, expectedRules);

        assertEquals(expectedGrammar.getRules(), newG.getRules());
        assertEquals(expectedGrammar.getRules(), newG.getRules());
        assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());
        assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
        assertEquals(expectedGrammar.getVariables(), newG.getVariables());
    }

    @Test
    public void testFNG() {
        Grammar newG = g.FNG(g, new AcademicSupport());
        boolean fng = true;
        for (com.ufla.lfapp.vo.grammar.Rule element : newG.getRules()) {
            int counter = 0;
            if (!element.getLeftSide().equals(newG.getInitialSymbol()) &&
                    element.getRightSide().equals("")) {
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
        for (com.ufla.lfapp.vo.grammar.Rule element : newG.getRules()) {
            int counter = 0;
            if (!element.getLeftSide().equals(newG.getInitialSymbol()) &&
                    element.getRightSide().equals("")) {
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

    private void initializeMatrix(Set<String>[][] m) {
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[i].length; j++) {
                m[i][j] = new TreeSet<>();
            }
        }
    }

    @Test
    public void testCYKAccept3() {
        g = g.FNC(g, new AcademicSupport());
        Set<String>[][] matrix = Grammar.CYK(g, "aba");

        assertNotNull(matrix);
        assertNotNull(matrix[0][0]);
        assertNotEquals("", matrix[0][0]);


        //[ [U, S', T1, T7], [],          []          ]
        //[ [T],             [T],         []          ]
        //[ [A', S', U],     [B', S', V], [A', S', U] ]
        //[ [a],             [b],         [a]         ]
        Set<String>[][] matrixExpected = new Set[4][3];
        initializeMatrix(matrixExpected);
        matrixExpected[3][0] = new TreeSet<>(Arrays.asList(new String[]
                {"a"}));
        matrixExpected[3][1] = new TreeSet<>(Arrays.asList(new String[]
                {"b"}));
        matrixExpected[3][2] = new TreeSet<>(Arrays.asList(new String[]
                {"a"}));
        matrixExpected[2][0] = new TreeSet<>(Arrays.asList(new String[]
                {"A'", "S'", "U"}));
        matrixExpected[2][1] = new TreeSet<>(Arrays.asList(new String[]
                {"B'", "S'", "V"}));
        matrixExpected[2][2] = new TreeSet<>(Arrays.asList(new String[]
                {"A'", "S'", "U"}));
        matrixExpected[1][0] = new TreeSet<>(Arrays.asList(new String[]
                {"T"}));
        matrixExpected[1][1] = new TreeSet<>(Arrays.asList(new String[]
                {"T"}));
        matrixExpected[0][0] = new TreeSet<>(Arrays.asList(new String[]
                {"U", "S'", "T7", "T1"}));
        Set<String> topVariables = matrix[0][0];

        assertArrayEquals(matrixExpected, matrix);
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
