package com.ufla.lfapp.vo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.MainActivity;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class RuleTest {

    Rule rule;

    @Test
    public void testRightSideContainsSymbol() {
        rule = new Rule("S", "BaA");
        assertTrue(rule.rightSideContainsSymbol("a"));
        assertTrue(rule.rightSideContainsSymbol("A"));
        assertTrue(rule.rightSideContainsSymbol("B"));
        assertFalse(rule.rightSideContainsSymbol("b"));
    }

    @Test
    public void testRightSideContainsSymbol2() {
        rule = new Rule("S", "Z120T200A1K340");
        assertTrue(rule.rightSideContainsSymbol("Z120"));
        assertTrue(rule.rightSideContainsSymbol("T200"));
        assertTrue(rule.rightSideContainsSymbol("A1"));
        assertTrue(rule.rightSideContainsSymbol("K340"));
        assertFalse(rule.rightSideContainsSymbol("Z"));
        assertFalse(rule.rightSideContainsSymbol("Z1"));
        assertFalse(rule.rightSideContainsSymbol("Z12"));
        assertFalse(rule.rightSideContainsSymbol("T"));
        assertFalse(rule.rightSideContainsSymbol("T2"));
        assertFalse(rule.rightSideContainsSymbol("T20"));
        assertFalse(rule.rightSideContainsSymbol("A"));
        assertFalse(rule.rightSideContainsSymbol("K"));
        assertFalse(rule.rightSideContainsSymbol("K3"));
        assertFalse(rule.rightSideContainsSymbol("K34"));
    }

    @Test
    public void testIsFngLambda() {
        rule = new Rule("S", "λ");
        assertTrue(rule.isFng("S"));
        assertFalse(rule.isFng("A"));
    }

    @Test
    public void testIsFngInitialSymbol() {
        rule = new Rule("S", "aS200S345");
        assertFalse(rule.isFng("S200"));
        assertFalse(rule.isFng("S345"));
        assertTrue(rule.isFng("S"));
        assertTrue(rule.isFng("S2"));
        assertTrue(rule.isFng("S20"));
        assertTrue(rule.isFng("S34"));
        assertTrue(rule.isFng("S3"));
        assertTrue(rule.isFng("A"));
    }

    @Test
    public void testIsFngSimpleRule() {
        rule = new Rule("S", "a");
        assertTrue(rule.isFng("S"));

        rule = new Rule("S", "A");
        assertFalse(rule.isFng("S"));
    }

    @Test
    public void testIsFng() {
        rule = new Rule("S", "ABCD");
        assertFalse(rule.isFng("S"));

        rule = new Rule("S", "ABabCD");
        assertFalse(rule.isFng("S"));

        rule = new Rule("S", "ABabCDcd");
        assertFalse(rule.isFng("S"));

        rule = new Rule("S", "aAaB");
        assertFalse(rule.isFng("S"));

        rule = new Rule("S", "aABabCD");
        assertFalse(rule.isFng("S"));

        rule = new Rule("S", "aABabCDab");
        assertFalse(rule.isFng("S"));

        rule = new Rule("S", "aT120abZ12abK400");
        assertFalse(rule.isFng("S"));

        rule = new Rule("S", "aA");
        assertTrue(rule.isFng("S"));

        rule = new Rule("S", "aABCD");
        assertTrue(rule.isFng("S"));

        rule = new Rule("S", "aT120Z12K400");
        assertTrue(rule.isFng("S"));
    }

    @Test
    public void testIsFncLambda() {
        rule = new Rule("S1", "λ");
        assertTrue(rule.isFnc("S1"));
        assertFalse(rule.isFnc("S"));
    }

    @Test
    public void testIsFncInitialSymbol() {
        rule = new Rule("S", "AS200");
        assertFalse(rule.isFnc("S200"));
        assertFalse(rule.isFnc("A"));
        assertTrue(rule.isFnc("S"));
        assertTrue(rule.isFnc("S2"));
        assertTrue(rule.isFnc("S20"));
    }

    @Test
    public void testIsFncSimpleRule() {
        rule = new Rule("S", "a");
        assertTrue(rule.isFnc("S"));

        rule = new Rule("S", "A1");
        assertFalse(rule.isFnc("S"));
    }

    @Test
    public void testIsFnc() {
        rule = new Rule("S", "aA");
        assertFalse(rule.isFnc("S"));

        rule = new Rule("S", "A12345");
        assertFalse(rule.isFnc("S"));

        rule = new Rule("S", "A12345a");
        assertFalse(rule.isFnc("S"));

        rule = new Rule("S", "A12345A12345A");
        assertFalse(rule.isFnc("S"));

        rule = new Rule("S", "A12345A12345A12");
        assertFalse(rule.isFnc("S"));

        rule = new Rule("S", "A12345A12345a");
        assertFalse(rule.isFnc("S"));

        rule = new Rule("S", "A12345A");
        assertTrue(rule.isFnc("S"));

        rule = new Rule("S", "A12345A12345");
        assertTrue(rule.isFnc("S"));
    }

    @Test
    public void testExistsLeftRecursion() {
        rule = new Rule("S1", "S");
        assertFalse(rule.existsLeftRecursion());

        rule = new Rule("S1", "S2");
        assertFalse(rule.existsLeftRecursion());

        rule = new Rule("S1", "S12");
        assertFalse(rule.existsLeftRecursion());

        rule = new Rule("S1", "S123");
        assertFalse(rule.existsLeftRecursion());

        rule = new Rule("S1", "S1");
        assertTrue(rule.existsLeftRecursion());

        rule = new Rule("S12345", "S12345");
        assertTrue(rule.existsLeftRecursion());
    }

    @Test
    public void testGetFirstVariableOfRightSide() {
        rule = new Rule("S", "aABC");
        assertEquals(null, rule.getFirstVariableOfRightSide());

        rule = new Rule("S", "ABC");
        assertEquals("A", rule.getFirstVariableOfRightSide());

        rule = new Rule("S", "AaBC");
        assertEquals("A", rule.getFirstVariableOfRightSide());

        rule = new Rule("S", "A1BC");
        assertEquals("A1", rule.getFirstVariableOfRightSide());

        rule = new Rule("S", "A12345B3C");
        assertEquals("A12345", rule.getFirstVariableOfRightSide());
    }

    @Test
    public void testGetSymbolsOfRightSide() {
        String[] expectedSymbols;

        rule = new Rule("S", "λ");
        expectedSymbols = new String[] { "λ" };
        assertEquals(new HashSet<>(Arrays.asList(expectedSymbols)),
                rule.getSymbolsOfRightSide());

        rule = new Rule("S", "aAb");
        expectedSymbols = new String[] { "a", "A", "b" };
        assertEquals(new HashSet<>(Arrays.asList(expectedSymbols)),
                rule.getSymbolsOfRightSide());

        rule = new Rule("S", "aA123bB12345CD12");
        expectedSymbols = new String[] { "a", "A123", "b", "B12345", "C",
                "D12" };
        assertEquals(new HashSet<>(Arrays.asList(expectedSymbols)),
                rule.getSymbolsOfRightSide());
    }

    @Test
    public void testExistsRecursion() {
        rule = new Rule("S10", "S1ABac");
        assertFalse(rule.existsRecursion());

        rule = new Rule("S10", "ABSA");
        assertFalse(rule.existsRecursion());

        rule = new Rule("S10", "ABS1A");
        assertFalse(rule.existsRecursion());

        rule = new Rule("S10", "ABS10A");
        assertTrue(rule.existsRecursion());

        rule = new Rule("S10", "S10ABA");
        assertTrue(rule.existsRecursion());

        rule = new Rule("S10", "ABAS10");
        assertTrue(rule.existsRecursion());
    }

    @Test
    public void testProducesLambda() {
        rule = new Rule("S", "aBSA");
        assertFalse(rule.producesLambda());

        rule = new Rule("S", "a");
        assertFalse(rule.producesLambda());

        rule = new Rule("S", "λ");
        assertTrue(rule.producesLambda());
    }

    @Test
    public void testProducesTerminalDirectly() {
        rule = new Rule("S", "λ");
        assertFalse(rule.producesTerminalDirectly());

        rule = new Rule("S", "A");
        assertFalse(rule.producesTerminalDirectly());

        rule = new Rule("S", "aA");
        assertFalse(rule.producesTerminalDirectly());

        rule = new Rule("S", "aabAabab");
        assertFalse(rule.producesTerminalDirectly());

        rule = new Rule("S", "aABCB");
        assertFalse(rule.producesTerminalDirectly());

        rule = new Rule("S", "a");
        assertTrue(rule.producesTerminalDirectly());

        rule = new Rule("S", "abc");
        assertTrue(rule.producesTerminalDirectly());

        rule = new Rule("S", "aaabcabc");
        assertTrue(rule.producesTerminalDirectly());
    }

    @Test
    public void testIsChainRule() {
        rule = new Rule("S", "a");
        assertFalse(rule.isChainRule());

        rule = new Rule("S", "aABC");
        assertFalse(rule.isChainRule());

        rule = new Rule("S", "AaBb");
        assertFalse(rule.isChainRule());

        rule = new Rule("S", "AabBbCaD");
        assertFalse(rule.isChainRule());

        rule = new Rule("S", "A");
        assertTrue(rule.isChainRule());

        rule = new Rule("S", "S");
        assertTrue(rule.isChainRule());

        rule = new Rule("S", "A12345");
        assertTrue(rule.isChainRule());
    }

}
