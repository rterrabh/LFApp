package com.ufla.lfapp.core.grammar;

import com.ufla.lfapp.utils.ResourcesContext;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class Rule06Test {

    Rule rule;
    // S -> aAB

    @BeforeClass
    public static void setTest() {
        ResourcesContext.isTest = true;
    }

    @Before
    public void setUp() {
        rule = new Rule("S", "aAB");
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
        assertTrue(rule.isFng("S"));
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
        assertEquals(null, rule.getFirstVariableOfRightSide());
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
