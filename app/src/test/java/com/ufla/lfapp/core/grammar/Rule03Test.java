package com.ufla.lfapp.core.grammar;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class Rule03Test {

    com.ufla.lfapp.core.grammar.Rule rule;
    // S -> S

    @Before
    public void setUp() {
        rule = new com.ufla.lfapp.core.grammar.Rule("S", "S");
    }

    @Test
    public void testClone1() {
        com.ufla.lfapp.core.grammar.Rule rc = (com.ufla.lfapp.core.grammar.Rule) rule.clone();
        assertEquals(rc, rule);
        rc.setRightSide("abr");
        assertNotEquals(rc, rule);
    }

    @Test
    public void testClone2() {
        com.ufla.lfapp.core.grammar.Rule rc = (com.ufla.lfapp.core.grammar.Rule) rule.clone();
        assertEquals(rc, rule);
        rc.setLeftSide("A");
        assertNotEquals(rc, rule);
    }

    @Test
    public void testIsFnc1() {
        assertFalse(rule.isFnc("S"));
    }

    @Test
    public void testIsFnc2() {
        assertFalse(rule.isFnc("A"));
    }

    @Test
    public void testIsFng1() {
        assertFalse(rule.isFng("S"));
    }

    @Test
    public void testIsFng2() {
        assertFalse(rule.isFng("A"));
    }

    @Test
    public void testExistsLeftRecursion() {
        assertTrue(rule.existsLeftRecursion());
    }

    @Test
    public void testGetFirstVariableOfRightSide() {
        assertEquals("S", rule.getFirstVariableOfRightSide());
    }

    @Test
    public void testGetSymbolsOfRightSide() {
        String[] expectedSymbolsStr = { "S" };
        Set<String> expectedSymbols = new HashSet<>(Arrays.asList
                (expectedSymbolsStr));
        assertEquals(expectedSymbols, rule.getSymbolsOfRightSide());
    }

    @Test
    public void testExistsRecursion() {
        assertTrue(rule.existsRecursion());
    }

    @Test
    public void testProducesLambda() {
        assertFalse(rule.producesLambda());
    }

    @Test
    public void testProducesTerminalDirectly() {
        assertFalse(rule.producesTerminalDirectly());
    }

    @Test
    public void testIsChainRule() {
        assertTrue(rule.isChainRule());
    }

}