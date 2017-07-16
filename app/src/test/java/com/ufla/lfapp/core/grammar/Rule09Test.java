package com.ufla.lfapp.core.grammar;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class Rule09Test {

    Rule rule;
    // Z120 -> Z1200T300Z12005

    @Before
    public void setUp() {
        rule = new Rule("Z120", "Z1200T300Z12005");
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
        assertFalse(rule.isFnc("Z120"));
    }

    @Test
    public void testIsFnc2() {
        assertFalse(rule.isFnc("T1"));
    }

    @Test
    public void testIsFng1() {
        assertFalse(rule.isFng("Z120"));
    }

    @Test
    public void testIsFng2() {
        assertFalse(rule.isFng("T1"));
    }

    @Test
    public void testExistsLeftRecursion() {
        assertFalse(rule.existsLeftRecursion());
    }

    @Test
    public void testGetFirstVariableOfRightSide() {
        assertEquals("Z1200", rule.getFirstVariableOfRightSide());
    }

    @Test
    public void testGetSymbolsOfRightSide() {
        String[] expectedSymbolsStr = { "Z1200", "T300", "Z12005" };
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
