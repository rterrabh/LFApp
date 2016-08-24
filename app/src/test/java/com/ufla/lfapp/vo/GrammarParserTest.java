package com.ufla.lfapp.vo;


import org.junit.Test;

import java.util.regex.Pattern;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GrammarParserTest {

    private Pattern pGrammarRegex = Pattern.compile(GrammarParser.GRAMMAR_REGEX);
    private Pattern pRuleElementRegex = Pattern.compile(GrammarParser.RULE_ELEMENT_RIGHT_REGEX);
    Pattern pRuleRegex = Pattern.compile(GrammarParser.RULE_REGEX);

    @Test
    public void testPatternRuleElement01() {
        assertTrue(pRuleElementRegex.matcher("A").matches());
    }

    @Test
    public void testPatternRuleElement02() {
        assertTrue(pRuleElementRegex.matcher("G1").matches());
    }

    @Test
    public void testPatternRuleElement03() {
        assertTrue(pRuleElementRegex.matcher("Z'aAZ1").matches());
    }

    @Test
    public void testPatternRuleElement04() {
        assertTrue(pRuleElementRegex.matcher("Ab").matches());
    }

    @Test
    public void testPatternRuleElement05() {
        assertFalse(pRuleElementRegex.matcher("1E").matches());
    }

    @Test
    public void testPatternRuleElement06() {
        assertFalse(pRuleElementRegex.matcher("Z55332'").matches());
    }

    @Test
    public void testPatternRuleElement07() {
        assertFalse(pRuleElementRegex.matcher("Z''''").matches());
    }

    @Test
    public void testPatternRuleElement08() {
        assertFalse(pRuleElementRegex.matcher("").matches());
    }

    @Test
    public void testPatternRuleGrammar01() {
        assertTrue(pRuleRegex.matcher("S -> AB | BC | BV").matches());
        assertTrue(pRuleRegex.matcher("A -> BA | a").matches());
        assertTrue(pRuleRegex.matcher("B -> aa | VC | CV | CC | b").matches());
        assertTrue(pRuleRegex.matcher("C -> AB").matches());
        assertTrue(pRuleRegex.matcher("V -> a").matches());
    }

    @Test
    public void testPatternRuleGrammar02() {
        assertTrue(pRuleRegex.matcher("S' -> XT18 | XT10 | UT10 | T2U | T2X | XT2 | a | UT2 | " +
                "XT7 | XT3 | VT3 | T1V | T1X | XT1 | b | VT1").matches());
        assertTrue(pRuleRegex.matcher("U -> XT18 | XT10 | UT10 | T2U | T2X | XT2 | a | UT2")
                .matches());
        assertTrue(pRuleRegex.matcher("V -> XT7 | XT3 | VT3 | T1V | T1X | XT1 | b | VT1")
                .matches());
        assertTrue(pRuleRegex.matcher("X -> T2T11 | T1T9 | T2T3 | T2T4 | T2T1 | T1T10 | T1T5 " +
                "| T1T2").matches());
        assertTrue(pRuleRegex.matcher("T1 -> b").matches());
        assertTrue(pRuleRegex.matcher("T2 -> a").matches());
        assertTrue(pRuleRegex.matcher("T3 -> T1X").matches());
        assertTrue(pRuleRegex.matcher("T4 -> XT1").matches());
        assertTrue(pRuleRegex.matcher("T5 -> XT2").matches());
        assertTrue(pRuleRegex.matcher("T7 -> T1V").matches());
        assertTrue(pRuleRegex.matcher("T9 -> XT10").matches());
        assertTrue(pRuleRegex.matcher("T10 -> T2X").matches());
        assertTrue(pRuleRegex.matcher("T11 -> XT3").matches());
        assertTrue(pRuleRegex.matcher("T18 -> T2U").matches());
    }

    @Test
    public void testPatternRuleGrammar03() {
        assertTrue(pRuleRegex.matcher("S -> λ | cBB | aB | cBZ1B | aZ1B").matches());
        assertTrue(pRuleRegex.matcher("A -> cB | a | cBZ1 | aZ1").matches());
        assertTrue(pRuleRegex.matcher("B -> b | cBB | aB | cBZ1B | aZ1B").matches());
        assertTrue(pRuleRegex.matcher("C -> c | cBC | aC | cBZ1C | aZ1C").matches());
        assertTrue(pRuleRegex.matcher("Z1 -> b | cBB | aB | cBZ1B | aZ1B | cB | cBCB | aCB | " +
                "cBZ1CB | aZ1CB | bZ1 | cBBZ1 | aBZ1 | cBZ1BZ1 | aZ1BZ1 | cBZ1 | cBCBZ1 | " +
                "aCBZ1 | cBZ1CBZ1 | aZ1CBZ1").matches());
    }

    @Test
    public void testPatternRuleGrammar04() {
        assertTrue(pRuleRegex.matcher("S -> U | V | S").matches());
        assertTrue(pRuleRegex.matcher("U -> XaU | XaX | UaX").matches());
        assertTrue(pRuleRegex.matcher("V -> XbV | XbX | VbX").matches());
        assertTrue(pRuleRegex.matcher("X -> aXbX | bXaX | λ | A").matches());
        assertTrue(pRuleRegex.matcher("A -> aA | B").matches());
        assertTrue(pRuleRegex.matcher("B -> bB | A").matches());
        assertTrue(pRuleRegex.matcher("C -> CD | a").matches());
        assertTrue(pRuleRegex.matcher("D -> DC | b").matches());
    }

    @Test
    public void testPatternRuleGrammar05() {
        assertTrue(pRuleRegex.matcher("S -> aS | bB").matches());
        assertTrue(pRuleRegex.matcher("B -> aEE | CDE").matches());
        assertTrue(pRuleRegex.matcher("C -> aBa | D ").matches());
        assertTrue(pRuleRegex.matcher("D -> aEa | λ").matches());
        assertTrue(pRuleRegex.matcher("E -> aDD | DC").matches());
    }

    @Test
    public void testPatternRuleGrammar06() {
        assertTrue(pRuleRegex.matcher("A -> Aa | Ab | b | c").matches());
    }

    @Test
    public void testPatternRuleGrammar07() {
        assertTrue(pRuleRegex.matcher("A -> Aa | Aab | bb | b").matches());
    }

    @Test
    public void testPatternRuleGrammar08() {
        assertTrue(pRuleRegex.matcher("A -> AB | BA | a").matches());
        assertTrue(pRuleRegex.matcher("B -> b | c").matches());
    }

    @Test
    public void testPatternRuleGrammar09() {
        assertTrue(pRuleRegex.matcher("S -> AB | λ").matches());
        assertTrue(pRuleRegex.matcher("A -> AB | CB | a").matches());
        assertTrue(pRuleRegex.matcher("B -> AB | b").matches());
        assertTrue(pRuleRegex.matcher("C -> AC | c").matches());
    }

    @Test
    public void testPatternRuleGrammar10() {
        assertTrue(pRuleRegex.matcher("S -> AT | AB").matches());
        assertTrue(pRuleRegex.matcher("T -> XB").matches());
        assertTrue(pRuleRegex.matcher("X -> AT | AB").matches());
        assertTrue(pRuleRegex.matcher("A -> a").matches());
        assertTrue(pRuleRegex.matcher("B -> b").matches());
    }

    @Test
    public void testPatternRuleGrammar11() {
        assertTrue(pRuleRegex.matcher("S -> aS | ABC").matches());
        assertTrue(pRuleRegex.matcher("A -> BB | λ").matches());
        assertTrue(pRuleRegex.matcher("B -> CC | a").matches());
        assertTrue(pRuleRegex.matcher("C -> AA | b").matches());
    }

    @Test
    public void testPatternRuleGrammar12() {
        assertTrue(pRuleRegex.matcher("S -> ABC | SA | A").matches());
        assertTrue(pRuleRegex.matcher("A -> aA | a").matches());
        assertTrue(pRuleRegex.matcher("B -> Sb | λ").matches());
        assertTrue(pRuleRegex.matcher("C -> cdC | dC | e").matches());
    }

    @Test
    public void testPatternRuleGrammar13() {
        assertTrue(pRuleRegex.matcher("S -> aS | AB | AC").matches());
        assertTrue(pRuleRegex.matcher("A -> aA | λ").matches());
        assertTrue(pRuleRegex.matcher("B -> bB | b").matches());
        assertTrue(pRuleRegex.matcher("C -> cC | λ").matches());
    }

    @Test
    public void testPatternRuleGrammar14() {
        assertTrue(pRuleRegex.matcher("S -> aSCbc | λ").matches());
        assertTrue(pRuleRegex.matcher("Cb -> bc").matches());
    }

    @Test
    public void testPatternRuleGrammar15() {
        assertTrue(pRuleRegex.matcher("S -> aAc | bc").matches());
        assertTrue(pRuleRegex.matcher("Ac -> b").matches());
    }

    @Test
    public void testPatternRuleGrammar16() {
        assertTrue(pRuleRegex.matcher("S -> aAbc | abc").matches());
        assertTrue(pRuleRegex.matcher("A -> aAbC | abC").matches());
        assertTrue(pRuleRegex.matcher("Cb -> bC").matches());
        assertTrue(pRuleRegex.matcher("Cc -> cc").matches());
    }

    @Test
    public void testPatternRuleGrammar17() {
        assertTrue(pRuleRegex.matcher("S -> SBA | a").matches());
        assertTrue(pRuleRegex.matcher("BA -> AB").matches());
        assertTrue(pRuleRegex.matcher("aA -> aaB").matches());
        assertTrue(pRuleRegex.matcher("B -> b").matches());
    }

    @Test
    public void testPatternRuleGrammar18() {
        assertTrue(pRuleRegex.matcher("S -> aTa | bTb").matches());
        assertTrue(pRuleRegex.matcher("T -> aTA | bTB").matches());
        assertTrue(pRuleRegex.matcher("Aa -> aA").matches());
        assertTrue(pRuleRegex.matcher("Ab -> bA").matches());
        assertTrue(pRuleRegex.matcher("Ba -> aB").matches());
        assertTrue(pRuleRegex.matcher("Bb -> bB").matches());
        assertTrue(pRuleRegex.matcher("A -> a").matches());
        assertTrue(pRuleRegex.matcher("B -> b").matches());
    }

    @Test
    public void testPatternRuleGrammar19() {
        assertTrue(pRuleRegex.matcher("S -> aAbc | λ").matches());
        assertTrue(pRuleRegex.matcher("A -> aAbC | λ").matches());
        assertTrue(pRuleRegex.matcher("Cb -> bC").matches());
        assertTrue(pRuleRegex.matcher("CC -> Cc").matches());
    }

    @Test
    public void testPatternRuleGrammar20() {
        assertTrue(pRuleRegex.matcher("S -> X | Y | aPAbQb").matches());
        assertTrue(pRuleRegex.matcher("X -> aaX | λ").matches());
        assertTrue(pRuleRegex.matcher("Y -> bbY | λ").matches());
        assertTrue(pRuleRegex.matcher("P -> aPA | λ").matches());
        assertTrue(pRuleRegex.matcher("Q -> bQb | E").matches());
        assertTrue(pRuleRegex.matcher("Ab -> bA").matches());
        assertTrue(pRuleRegex.matcher("AE -> a").matches());
        assertTrue(pRuleRegex.matcher("Aa -> aa").matches());
    }

    @Test
    public void testPatternRuleGrammarFail01() {
        assertFalse(pRuleRegex.matcher("Asasd ->").matches());
        assertTrue(pRuleRegex.matcher("ab -> aA").matches());
    }

    @Test
    public void testPatternRuleGrammarFail02() {
        assertTrue(pRuleRegex.matcher("ab -> ABa").matches());
        assertFalse(pRuleRegex.matcher("t2 -> aA").matches());
        assertFalse(pRuleRegex.matcher("t' -> b").matches());
    }

    @Test
    public void testPatternRuleGrammarFail03() {
        assertTrue(pRuleRegex.matcher("S -> X | Y").matches());
        assertFalse(pRuleRegex.matcher("λ -> ab").matches());
    }

    @Test
    public void testPatternRuleGrammarFail04() {
        assertTrue(pRuleRegex.matcher("S -> A").matches());
        assertFalse(pRuleRegex.matcher("334 -> aA").matches());
    }

    @Test
    public void testPatternGrammar01() {
<<<<<<< HEAD
        assertTrue(pGrammarRegex.matcher(
                "S -> AB | BC | BV\n" +
                        "A -> BA | a \n" +
=======
        String N = ((Character)((char) 10)).toString();
        char a = '|';
        int b = a;
        System.out.println(b);
        assertTrue(pGrammarRegex.matcher(
                "S -> AB | BC | BV\n" +
                        "A -> BA | a " + N +
>>>>>>> c4c20405093392ad913db2b633c8f988640d5918
                        "B -> aa | VC | CV | CC | b\n" +
                        "C -> AB\n" +
                        "V -> a").
                matches());
    }

    @Test
    public void testPatternGrammar02() {
        assertTrue(pGrammarRegex.matcher(
                "S -> aS | AB | AC\n" +
                        "A -> aA | λ\n" +
                        "B -> bB | b\n" +
                        "C -> cC | λ")
                .matches());
    }

    @Test
    public void testPatternGrammar03() {
        assertTrue(pGrammarRegex.matcher(
                "S -> ABC | SA | A\n" +
                        "A -> aA | a\n" +
                        "B -> Sb | λ\n" +
                        "C -> cdC | dC | e")
                .matches());
    }

    @Test
    public void testPatternGrammar04() {
        assertTrue(pGrammarRegex.matcher(
                "S -> X | Y | aPAbQb\n" +
                        "X -> aaX | λ\n" +
                        "Y -> bbY | λ\n" +
                        "P -> aPA | λ\n" +
                        "Q -> bQb | E\n" +
                        "Ab -> bA\n" +
                        "AE -> a\n" +
                        "Aa -> aa")
                .matches());
    }

    @Test
    public void testPatternGrammar05() {
        assertTrue(pGrammarRegex.matcher(
                "S -> aAbc | λ\n" +
                        "A -> aAbC | λ\n" +
                        "Cb -> bC\n" +
                        "CC -> Cc")
                .matches());
    }

    @Test
    public void testPatternGrammar06() {
        assertTrue(pGrammarRegex.matcher(
                "S -> aTa | bTb\n" +
                        "T -> aTA | bTB\n" +
                        "Aa -> aA\n" +
                        "Ab -> bA\n" +
                        "Ba -> aB\n" +
                        "Bb -> bB\n" +
                        "A -> a\n" +
                        "B -> b")
                .matches());

    }

    @Test
    public void testPatternGrammar07() {
        assertTrue(pGrammarRegex.matcher(
                "S -> SBA | a\n" +
                        "BA -> AB\n" +
                        "aA -> aaB\n" +
                        "B -> b")
                .matches());
    }

    @Test
    public void testPatternGrammar08() {
        assertTrue(pGrammarRegex.matcher(
                "S -> aAbc | abc\n" +
                        "A -> aAbC | abC\n" +
                        "Cb -> bC\n" +
                        "Cc -> cc")
                .matches());
    }

    @Test
    public void testPatternGrammar09() {
        assertTrue(pGrammarRegex.matcher(
                "S -> aAc | bc\n" +
                        "Ac -> b").
                matches());
    }

    @Test
    public void testPatternGrammar10() {
        assertTrue(pGrammarRegex.matcher(
                "S -> aSCbc | λ\n" +
                        "Cb -> bc")
                .matches());
    }

    @Test
    public void testPatternGrammar11() {
        assertTrue(pGrammarRegex.matcher(
                "S -> λ | cBB | aB | cBZ1B | aZ1B\n" +
                        "A -> cB | a | cBZ1 | aZ1\n" +
                        "B -> b | cBB | aB | cBZ1B | aZ1B\n" +
                        "C -> c | cBC | aC | cBZ1C | aZ1C\n" +
                        "Z1 -> b | cBB | aB | cBZ1B | aZ1B | cB | cBCB | aCB | cBZ1CB | aZ1CB | " +
                        "bZ1 | cBBZ1 | aBZ1 | cBZ1BZ1 | aZ1BZ1 | cBZ1 | cBCBZ1 | aCBZ1 | " +
                        "cBZ1CBZ1 | aZ1CBZ1")
                .matches());
    }

    @Test
    public void testPatternGrammar12() {
        assertTrue(pGrammarRegex.matcher(
                "S' -> XT18 | XT10 | UT10 | T2U | T2X | XT2 | a | UT2 | XT7 | XT3 | VT3 | T1V | " +
                        "T1X | XT1 | b | VT1\n" +
                        "U -> XT18 | XT10 | UT10 | T2U | T2X | XT2 | a | UT2\n" +
                        "V -> XT7 | XT3 | VT3 | T1V | T1X | XT1 | b | VT1\n" +
                        "X -> T2T11 | T1T9 | T2T3 | T2T4 | T2T1 | T1T10 | T1T5 | T1T2\n" +
                        "T1 -> b\n" +
                        "T2 -> a\n" +
                        "T3 -> T1X\n" +
                        "T4 -> XT1\n" +
                        "T5 -> XT2\n" +
                        "T9 -> XT10\n" +
                        "T10 -> T2X\n" +
                        "T11 -> XT3\n" +
                        "T18 -> T2U")
                .matches());
    }

    @Test
    public void testPatternGrammar13() {
        assertTrue(pGrammarRegex.matcher(
                "S -> U | V | S\n" +
                        "U -> XaU | XaX | UaX\n" +
                        "V -> XbV | XbX | VbX\n" +
                        "X -> aXbX | bXaX | λ | A\n" +
                        "A -> aA | B\n" +
                        "B -> bB | A\n" +
                        "C -> CD | a\n" +
                        "D -> DC | b")
                .matches());
    }

    @Test
    public void testPatternGrammar14() {
        assertTrue(pGrammarRegex.matcher(
                "S -> aS | bB\n" +
                        "B -> aEE | CDE\n" +
                        "C -> aBa | D \n" +
                        "D -> aEa | λ\n" +
                        "E -> aDD | DC")
                .matches());
    }

    @Test
    public void testPatternGrammar15() {
        assertTrue(pGrammarRegex.matcher(
                "S -> ABC | SA | A\n" +
                        "A -> aA | a\n" +
                        "B -> Sb | λ\n" +
                        "C -> cdC | dC | e")
                .matches());
    }

    @Test
    public void testPatternGrammar16() {
        assertTrue(pGrammarRegex.matcher(
                "S -> aS | ABC\n" +
                        "A -> BB | λ\n" +
                        "B -> CC | a\n" +
                        "C -> AA | b   	\n	\n")
                .matches());
    }

    @Test
    public void testPatternGrammar17() {
        assertTrue(pGrammarRegex.matcher(
                "S -> AT | AB\n" +
                        "T -> XB\n" +
                        "X -> AT | AB\n" +
                        "A -> a\n" +
                        "B -> b\n")
                .matches());
    }

    @Test
    public void testPatternGrammar18() {
        assertTrue(pGrammarRegex.matcher(
                "S -> AB | λ\n" +
                        "A -> AB | CB | a\n" +
                        "B -> AB | b\n" +
                        "C -> AC | c\n")
                .matches());
    }

    @Test
    public void testPatternGrammar19() {
        assertTrue(pGrammarRegex.matcher(
                "A -> AB | BA | a\n" +
                        "B -> b | c\n")
                .matches());
    }

    @Test
    public void testPatternGrammar20() {
        assertTrue(pGrammarRegex.matcher(
                "A -> Aa | Aab | bb | b")
                .matches());
    }

    @Test
    public void testPatternGrammar21() {
        assertTrue(pGrammarRegex.matcher(
                "S -> aABC | a | S\n" +
                        "A -> aA | λ\n" +
                        "B -> bcB | bc\n" +
                        "C -> abc\n" +
                        "D -> ab | aa | ab\n" +
                        "E -> a")
                .matches());
<<<<<<< HEAD
=======
        assertTrue(Pattern.matches(GrammarParser.GRAMMAR_REGEX,
                "S -> aABC | a | S\n" +
                "A -> aA | λ\n" +
                "B -> bcB | bc\n" +
                "C -> abc\n" +
                "D -> ab | aa | ab\n" +
                "E -> a"));
>>>>>>> c4c20405093392ad913db2b633c8f988640d5918
    }

    @Test
    public void testPatternGrammarFail01() {
        assertFalse(pGrammarRegex.matcher(
                "Asasd ->\n" +
                        "ab -> aA")
                .matches());
    }

    @Test
    public void testPatternGrammarFail02() {
        assertFalse(pGrammarRegex.matcher(
                "ab -> ABa\n" +
                        "t2 -> aA\n" +
                        "t' -> b")
                .matches());
    }

    @Test
    public void testPatternGrammarFail03() {
        assertFalse(pGrammarRegex.matcher(
                "S -> X | Y\n" +
                        "λ -> ab")
                .matches());
    }

    @Test
    public void testPatternGrammarFail04() {
        assertFalse(pGrammarRegex.matcher(
                "S -> A\n" +
                        "334 -> aA")
                .matches());
    }

}
