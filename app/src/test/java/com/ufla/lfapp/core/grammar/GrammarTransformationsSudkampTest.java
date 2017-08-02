package com.ufla.lfapp.core.grammar;

import com.ufla.lfapp.core.grammar.parser.TreeDerivation;
import com.ufla.lfapp.core.grammar.parser.TreeDerivationParser;
import com.ufla.lfapp.utils.ResourcesContext;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.Test;

import java.util.Set;

/**
 * Created by carlos on 01/08/17.
 */

public class GrammarTransformationsSudkampTest {

    static {
        ResourcesContext.isTest = true;
    }

    // pg 105 FEATURE - Grammar - Elimination recursion of start symbol
    @Test
    public void example_4_1_1Test() {
        String grammarTxt =
                "S -> aS | AB | AC\n" +
                "A -> aA | λ\n" +
                "B -> bB | bS\n" +
                "C -> cC | λ";
        Grammar grammar = new Grammar(grammarTxt);
        String expectedGrammarTxt =
                "S' -> S\n" +
                "S -> aS | AB | AC\n" +
                "A -> aA | λ\n" +
                "B -> bB | bS\n" +
                "C -> cC | λ";
        Grammar expectedGrammar = new Grammar(expectedGrammarTxt);
        Grammar resultGrammar = grammar.getGrammarWithInitialSymbolNotRecursive(grammar,
                new AcademicSupport());
        assertEquals(expectedGrammar, resultGrammar);
    }

    // pg 109 FEATURE - Grammar - Elimination of λ-Rules
    @Test
    public void example_4_2_1Test() {
        String grammarTxt =
                "S -> ACA\n" +
                "A -> aAa | B | C\n" +
                "B -> bB | b\n" +
                "C -> cC | λ";
        Grammar grammar = new Grammar(grammarTxt);
        String expectedGrammarTxt =
                "S -> ACA | CA | AA | AC | A | C | λ\n" +
                "A -> aAa | aa | B | C\n" +
                "B -> bB | b\n" +
                "C -> cC | c";
        Grammar expectedGrammar = new Grammar(expectedGrammarTxt);
        Grammar resultGrammar = grammar.getGrammarEssentiallyNoncontracting(grammar,
                new AcademicSupport());
        assertEquals(expectedGrammar, resultGrammar);
    }

    // pg 113 FEATURE - Grammar - Elimination of λ-Rules
    @Test
    public void example_4_2_3Test() {
        String grammarTxt =
                "S -> ABC\n" +
                "A -> aA | λ\n" +
                "B -> bB | λ\n" +
                "C -> cC | λ";
        Grammar grammar = new Grammar(grammarTxt);
        String expectedGrammarTxt =
                "S -> ABC | AB | BC | AC | A | B | C | λ\n" +
                "A -> aA | a\n" +
                "B -> bB | b\n" +
                "C -> cC | c";
        Grammar expectedGrammar = new Grammar(expectedGrammarTxt);
        Grammar resultGrammar = grammar.getGrammarEssentiallyNoncontracting(grammar,
                new AcademicSupport());
        assertEquals(expectedGrammar, resultGrammar);
    }

    // pg 115 FEATURE - Grammar - Elimination of Chain Rules
    @Test
    public void example_4_3_1Test() {
        String grammarTxt =
                "S -> ACA | CA | AA | AC | A | C | λ\n" +
                "A -> aAa | aa | B | C\n" +
                "B -> bB | b\n" +
                "C -> cC | c";
        Grammar grammar = new Grammar(grammarTxt);
        String expectedGrammarTxt =
                "S -> ACA | CA | AA | AC | aAa | aa | bB | b | cC | c | λ\n" +
                "A -> aAa | aa | bB | b | cC | c\n" +
                "B -> bB | b\n" +
                "C -> cC | c";
        Grammar expectedGrammar = new Grammar(expectedGrammarTxt);
        Grammar resultGrammar = grammar.getGrammarWithoutChainRules(grammar,
                new AcademicSupport());
        assertEquals(expectedGrammar, resultGrammar);
    }

    // pag 118 FEATURE - Grammar - Useless Symbols - TERM
    @Test
    public void example_4_4_1Test() {
        String grammarTxt =
                "S -> AC | BS | B\n" +
                "A -> aA | aF\n" +
                "B -> CF | b\n" +
                "C -> cC | D\n" +
                "D -> aD | BD | C\n" +
                "E -> aA | BSA\n" +
                "F -> bB | b";
        Grammar grammar = new Grammar(grammarTxt);
        String expectedGrammarTxt =
                "S -> BS | B\n" +
                "A -> aA | aF\n" +
                "B -> b\n" +
                "E -> aA | BSA\n" +
                "F -> bB | b";
        Grammar expectedGrammar = new Grammar(expectedGrammarTxt);
        Grammar resultGrammar = grammar.getGrammarWithoutNoTerm(grammar,
                new AcademicSupport());
        assertEquals(expectedGrammar, resultGrammar);
    }

    // pag 120 FEATURE - Grammar - Useless Symbols - REACH
    @Test
    public void example_4_4_2Test() {
        String grammarTxt =
                "S -> BS | B\n" +
                "A -> aA | aF\n" +
                "B -> b\n" +
                "E -> aA | BSA\n" +
                "F -> bB | b";
        Grammar grammar = new Grammar(grammarTxt);
        String expectedGrammarTxt =
                "S -> BS | B\n" +
                "B -> b";
        Grammar expectedGrammar = new Grammar(expectedGrammarTxt);
        Grammar resultGrammar = grammar.getGrammarWithoutNoReach(grammar,
                new AcademicSupport());
        assertEquals(expectedGrammar, resultGrammar);
    }

    // pag 121 FEATURE - Grammar - Useless Symbols
    @Test
    public void example_4_4_3Test() {
        String grammarTxt =
                "S -> a | AB\n" +
                "A -> b";
        Grammar grammar = new Grammar(grammarTxt);
        String expectedGrammarTxt =
                "S -> a\n" +
                "A -> b";
        Grammar expectedGrammar = new Grammar(expectedGrammarTxt);
        Grammar resultGrammar = grammar.getGrammarWithoutNoTerm(grammar,
                new AcademicSupport());
        assertEquals(expectedGrammar, resultGrammar);

        expectedGrammarTxt =
                "S -> a";
        resultGrammar = resultGrammar.getGrammarWithoutNoReach(resultGrammar,
                new AcademicSupport());
        expectedGrammar = new Grammar(expectedGrammarTxt);
        assertEquals(expectedGrammar, resultGrammar);
    }

    // pag 122 FEATURE - Grammar - Chomsky Normal Form
    @Test
    public void example_4_5_1Test() {
        String grammarTxt =
                "S -> aABC | a\n" +
                "A -> aA| a\n" +
                "B -> bcB| bc\n" +
                "C -> cC| c";
        Grammar grammar = new Grammar(grammarTxt);
        String expectedGrammarTxt =
                "S -> A'T1 | a\n" +
                "A' -> a\n" +
                "T1 -> AT2\n" +
                "T2 -> BC\n" +
                "A -> A'A | a\n" +
                "B -> B'T3 | B'C'\n" +
                "T3 -> C'B\n" +
                "C -> C'C | c\n" +
                "B' -> b\n" +
                "C' -> c";
        Grammar expectedGrammar = new Grammar(expectedGrammarTxt);
        Grammar resultGrammar = grammar.FNC(grammar,
                new AcademicSupport());
        assertEquals(expectedGrammar, resultGrammar);
    }

    // pg 124 FEATURE - Grammar - Chomsky Normal Form
    @Test
    public void example_4_5_2Test() {
        String grammarTxt =
                "S -> aXb | ab\n" +
                "X -> aXb | ab";
        Grammar grammar = new Grammar(grammarTxt);
        String expectedGrammarTxt =
                "S -> A'T1 | A'B'\n" +
                "T1 -> XB'\n" +
                "X -> A'T1 | A'B'\n" +
                "A' -> a\n" +
                "B' -> b";
        Grammar expectedGrammar = new Grammar(expectedGrammarTxt);
        Grammar resultGrammar = grammar.FNC(grammar,
                new AcademicSupport());
        assertEquals(expectedGrammar, resultGrammar);
    }

    // pag 126 FEATURE - Grammar - CYK
    @Test
    public void table_4_1_CYKTest() {
        String grammarTxt =
                "S -> AT | AB\n" +
                "T -> XB\n" +
                "X -> AT | AB\n" +
                "A -> a\n" +
                "B -> b";
        String string = "aaabbb";
        Grammar grammar = new Grammar(grammarTxt);
        Set<String>[][] matrix = grammar.CYK(grammar, string);
        assertNotNull(matrix);
        assertNotNull(matrix[0][0]);
        assertNotEquals("", matrix[0][0]);

        Set<String> topVariables = matrix[0][0];

        assertTrue(topVariables.contains(grammar.getInitialSymbol()));
    }


    // pag 130 FEATURE - Grammar - Removal of Direct Left Recursion
    @Test
    public void example_4_7_1Test() {
        String grammarTxt =
                "A -> Aa | Aab | bb | b";
        Grammar grammar = new Grammar(grammarTxt);
        String expectedGrammarTxt =
                "A -> bb | b | bbR1 | bR1\n" +
                "R1 -> aR1 | abR1 | a | ab";
        Grammar expectedGrammar = new Grammar(expectedGrammarTxt);
        Grammar resultGrammar = grammar.removingTheImmediateLeftRecursion(grammar,
                new AcademicSupport());
        assertEquals(expectedGrammar, resultGrammar);
    }

    // pag 132 FEATURE - Grammar - Greibach Normal Form
    @Test
    public void example_4_8_0Test() {
        String grammarTxt =
                "S -> AB | λ\n" +
                "A -> AB | CB | a\n" +
                "B -> AB | b\n" +
                "C -> AC | c";
        Grammar grammar = new Grammar(grammarTxt);
        String expectedGrammarTxt =
                "S -> λ | " +
                    "aR1B | aB | " +
                    " aR1CBR1B | aCBR1B | cBR1B | aR1CR2BR1B | aCR2BR1B | cR2BR1B | " +
                    " aR1CBB | aCBB | cBB | aR1CR2BB | aCR2BB | cR2BB\n" +
                "A -> aR1 | a | " +
                    "aR1CBR1 | aCBR1 | cBR1 | aR1CR2BR1 | aCR2BR1 | cR2BR1 | " +
                    "aR1CB | aCB | cB | aR1CR2B | aCR2B | cR2B\n" +
                "B -> aR1B | aB | b | " +
                    "aR1CBR1B | aCBR1B | cBR1B | aR1CR2BR1B | aCR2BR1B | cR2BR1B | " +
                    "aR1CBB | aCBB | cBB | aR1CR2BB | aCR2BB | cR2BB\n" +
                "C -> aR1C | aC | c | aR1CR2 | aCR2 | cR2\n" +
                "R1 -> " +
                    "aR1B | aB | b | " +
                    "aR1CBR1B | aCBR1B | cBR1B | aR1CR2BR1B | aCR2BR1B | cR2BR1B | " +
                    "aR1CBB | aCBB | cBB | aR1CR2BB | aCR2BB | cR2BB | " +

                    "aR1BR1 | aBR1 | bR1 | " +
                    "aR1CBR1BR1 | aCBR1BR1 | cBR1BR1 | aR1CR2BR1BR1 | aCR2BR1BR1 | cR2BR1BR1 |" +
                    "aR1CBBR1 | aCBBR1 | cBBR1 | aR1CR2BBR1 | aCR2BBR1 | cR2BBR1\n" +
                "R2 -> " +
                    "aR1BR1CR2  | aBR1CR2  | bR1CR2  | " +
                    "aR1CBR1BR1CR2  | aCBR1BR1CR2  | cBR1BR1CR2  | aR1CR2BR1BR1CR2  | aCR2BR1BR1CR2  | cR2BR1BR1CR2  | " +
                    "aR1CBBR1CR2  | aCBBR1CR2  | cBBR1CR2  | aR1CR2BBR1CR2  | aCR2BBR1CR2  | cR2BBR1CR2  | " +

                    "aR1BCR2 | aBCR2 | bCR2 | " +
                    "aR1CBR1BCR2 | aCBR1BCR2 | cBR1BCR2 | aR1CR2BR1BCR2 | aCR2BR1BCR2 | cR2BR1BCR2 | " +
                    "aR1CBBCR2 | aCBBCR2 | cBBCR2 | aR1CR2BBCR2 | aCR2BBCR2 | cR2BBCR2 | " +

                    "aR1BR1C | aBR1C | bR1C | " +
                    "aR1CBR1BR1C | aCBR1BR1C | cBR1BR1C | aR1CR2BR1BR1C | aCR2BR1BR1C | cR2BR1BR1C | " +
                    "aR1CBBR1C | aCBBR1C | cBBR1C | aR1CR2BBR1C | aCR2BBR1C | cR2BBR1C | " +

                    "aR1BC | aBC | bC | " +
                    "aR1CBR1BC | aCBR1BC | cBR1BC | aR1CR2BR1BC | aCR2BR1BC | cR2BR1BC | " +
                    "aR1CBBC | aCBBC | cBBC | aR1CR2BBC | aCR2BBC | cR2BBC\n";
        Grammar expectedGrammar = new Grammar(expectedGrammarTxt);
        Grammar resultGrammar = grammar.FNGTerra(grammar,
                new AcademicSupport());
        assertEquals(expectedGrammar, resultGrammar);
    }

    // pag 136 FEATURE - Grammar - Greibach Normal Form
    @Test
    public void example_4_8_1Test() {
        String grammarTxt =
                "S -> SaB | aB\n" +
                "B -> bB | λ";
        Grammar grammar = new Grammar(grammarTxt);
        String expectedGrammarTxt =
                "S' -> S\n" +
                "S -> SaB | aB\n" +
                "B -> bB | λ";
        Grammar expectedGrammar = new Grammar(expectedGrammarTxt);
        Grammar resultGrammar = grammar.getGrammarWithInitialSymbolNotRecursive(grammar,
                new AcademicSupport());
        assertEquals(expectedGrammar, resultGrammar);

        expectedGrammarTxt =
                "S' -> S\n" +
                "S -> SaB | Sa | aB | a\n" +
                "B -> bB | b";
        expectedGrammar = new Grammar(expectedGrammarTxt);
        resultGrammar = resultGrammar.getGrammarEssentiallyNoncontracting(resultGrammar,
                new AcademicSupport());
        assertEquals(expectedGrammar, resultGrammar);

        expectedGrammarTxt =
                "S' -> SaB | Sa | aB | a\n" +
                "S -> SaB | Sa | aB | a\n" +
                "B -> bB | b";
        expectedGrammar = new Grammar(expectedGrammarTxt);
        resultGrammar = resultGrammar.getGrammarWithoutChainRules(resultGrammar,
                new AcademicSupport());
        assertEquals(expectedGrammar, resultGrammar);

        expectedGrammarTxt =
                "S' -> ST1 | SA' | A'B | a\n" +
                "S -> ST1 | SA' | A'B | a\n" +
                "B -> B'B | b\n" +
                "T1 -> A'B\n" +
                "A' -> a\n" +
                "B' -> b";
        expectedGrammar = new Grammar(expectedGrammarTxt);
        resultGrammar = resultGrammar.FNC(resultGrammar,
                new AcademicSupport());
        assertEquals(expectedGrammar, resultGrammar);

        expectedGrammarTxt =
                "S' -> aB | a | " +
                    "aBT1 | aT1 | aBR1T1 | aR1T1 | " +
                    "aBA' | aA' | aBR1A' | aR1A'\n" +
                "S -> aB | a | aBR1 | aR1\n" +
                "B -> bB | b\n" +
                "T1 -> aB\n" +
                "A' -> a\n" +
                "B' -> b\n" +
                "R1 -> aB | a | aBR1 | aR1";
        expectedGrammar = new Grammar(expectedGrammarTxt);
        resultGrammar = resultGrammar.FNGTerra(resultGrammar,
                new AcademicSupport());
        assertEquals(expectedGrammar, resultGrammar);
    }


}
