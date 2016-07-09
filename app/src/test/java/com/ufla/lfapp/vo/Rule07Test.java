package com.ufla.lfapp.vo;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;


public class Rule07Test {

    Rule rule;
    // S -> ABaB

    @Before
    public void setUp() {
        rule = new Rule("S", "ABaB");
    }

    @Test
    public void testClone1() {
        Rule rc = (Rule) rule.clone();
        assertEquals(rc, rule);
        rc.setRightSide("abr");
        assertNotEquals(rc, rule);
    }

    @Test
    public void testClone2() {
        Rule rc = (Rule) rule.clone();
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
        assertFalse(rule.existsLeftRecursion());
    }

    @Test
    public void testGetFirstVariableOfRightSide() {
        assertEquals("A", rule.getFirstVariableOfRightSide());
    }

    @Test
    public void testGetSymbolsOfRightSide() {
        String[] expectedSymbolsStr = { "a", "A", "B" };
        Set<String> expectedSymbols = new HashSet<>(Arrays.asList
                (expectedSymbolsStr));
        assertEquals(expectedSymbols, rule.getSymbolsOfRightSide());
    }

    @Test
    public void testExistsRecursion() {
        assertFalse(rule.existsRecursion());
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
        assertFalse(rule.isChainRule());
    }

}
