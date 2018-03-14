package com.ufla.lfapp.core.machine;

import com.ufla.lfapp.core.machine.fsa.FSAConfiguration;
import com.ufla.lfapp.core.machine.pda.PDAConfiguration;
import com.ufla.lfapp.core.machine.tm.TMConfiguration;
import com.ufla.lfapp.core.machine.tm.var.TMMultiTapeConfiguration;
import com.ufla.lfapp.core.machine.tm.var.TMMultiTrackConfiguration;
import com.ufla.lfapp.utils.ResourcesContext;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by carlos on 10/22/17.
 */

public class ConfigurationTest {

    @BeforeClass
    public static void setTest() {
        ResourcesContext.isTest = true;
    }

    @Test
    public void testFSAConfiguration1() {
        State q0 = new State("q0");
        String input = "aba";
        FSAConfiguration fsaConfiguration = new FSAConfiguration(null, q0, 0, input, 0);
        FSAConfiguration fsaConfiguration2 = new FSAConfiguration(null, q0, 0,
                new StringBuilder("aba"), 0);
        FSAConfiguration fsaConfiguration3 = new FSAConfiguration(fsaConfiguration2, q0, 0,
                new StringBuilder("aba"), 0);
        Set<FSAConfiguration> configurations = new HashSet<>();
        configurations.add(fsaConfiguration);
        configurations.add(fsaConfiguration3);
        assertEquals(null, fsaConfiguration.getPrevious());
        assertEquals(q0, fsaConfiguration.getState());
        assertEquals(0, fsaConfiguration.getIndex());
        assertEquals(input, fsaConfiguration.getInput());
        assertEquals("FSAConfiguration{configuration=Configuration{previous=null, state=q0, depth=0}, input='aba', index=0}",
                fsaConfiguration.toString());
        assertTrue(configurations.contains(fsaConfiguration2));
        assertTrue(configurations.contains(fsaConfiguration3));
        assertTrue(configurations.contains(fsaConfiguration));
        assertFalse(fsaConfiguration3.equals(fsaConfiguration));
    }

    @Test
    public void testPDAConfiguration1() {
        State q0 = new State("q0");
        String input = "aba";
        String stack = "A";
        PDAConfiguration pdaConfiguration = new PDAConfiguration(null, q0, 0, input, stack, 0);
        PDAConfiguration pdaConfiguration2 = new PDAConfiguration(null, q0, 0,
                new StringBuilder(input), new StringBuilder(stack), 0);
        Set<PDAConfiguration> configurations = new HashSet<>();
        configurations.add(pdaConfiguration);
        assertEquals(null, pdaConfiguration.getPrevious());
        assertEquals(q0, pdaConfiguration.getState());
        assertEquals(0, pdaConfiguration.getIndex());
        assertEquals(input, pdaConfiguration.getInput());
        assertEquals(stack, pdaConfiguration.getStack());
        assertEquals(stack, pdaConfiguration.nextStackSymbol());
        assertEquals("a", pdaConfiguration.nextSymbol());
        assertEquals("PDAConfiguration{configuration=Configuration{previous=null, state=q0, depth=0}, input='aba', stack='A', index=0}",
                pdaConfiguration.toString());
        assertTrue(configurations.contains(pdaConfiguration2));
    }

    @Test
    public void testTMConfiguration() {
        State q0 = new State("q0");
        String tape = "aba";
        TMConfiguration tmConfiguration = new TMConfiguration(null, q0, 0, tape, 0);
        TMConfiguration tmConfiguration2 = new TMConfiguration(null, q0, 0,
                new StringBuilder(tape), 0);
        Set<TMConfiguration> configurations = new HashSet<>();
        configurations.add(tmConfiguration);
        assertEquals(null, tmConfiguration.getPrevious());
        assertEquals(q0, tmConfiguration.getState());
        assertEquals(0, tmConfiguration.getIndex());
        assertEquals("TMConfiguration{configuration=Configuration{previous=null, state=q0, depth=0}, tape='aba', index=0}",
                tmConfiguration.toString());
        assertTrue(configurations.contains(tmConfiguration2));
    }

    @Test
    public void testTMMultiTrackConfiguration() {
        State q0 = new State("q0");
        String[] tapes = { "aba", "BBB" };
        StringBuilder[] tapesBuilder = new StringBuilder[2];
        tapesBuilder[0] = new StringBuilder(tapes[0]);
        tapesBuilder[1] = new StringBuilder(tapes[1]);
        TMMultiTrackConfiguration tmMultiTrackConfiguration = new TMMultiTrackConfiguration(null, q0, 0, tapes, 0);
        TMMultiTrackConfiguration tmMultiTrackConfiguration2 = new TMMultiTrackConfiguration(null, q0, 0,
                tapesBuilder, 0);
        Set<TMMultiTrackConfiguration> configurations = new HashSet<>();
        configurations.add(tmMultiTrackConfiguration);
        assertEquals(null, tmMultiTrackConfiguration.getPrevious());
        assertEquals(q0, tmMultiTrackConfiguration.getState());
        assertEquals(0, tmMultiTrackConfiguration.getIndex());
        assertArrayEquals(tapes, tmMultiTrackConfiguration2.getTapes());
        assertEquals("TMMultiTrackConfiguration{configuration=Configuration{previous=null, state=q0, depth=0}, tapes=[aba, BBB], index=0}",
                tmMultiTrackConfiguration.toString());
        assertTrue(configurations.contains(tmMultiTrackConfiguration2));
    }

    @Test
    public void testTMMultiTapeConfiguration() {
        State q0 = new State("q0");
        String[] tapes = { "aba", "BBB" };
        int[] indexes= {0, 0};
        StringBuilder[] tapesBuilder = new StringBuilder[2];
        tapesBuilder[0] = new StringBuilder(tapes[0]);
        tapesBuilder[1] = new StringBuilder(tapes[1]);
        TMMultiTapeConfiguration tmMultiTapeConfiguration = new TMMultiTapeConfiguration(null, q0, 0, tapes, indexes);
        TMMultiTapeConfiguration tmMultiTapeConfiguration2 = new TMMultiTapeConfiguration(null, q0, 0,
                tapesBuilder, indexes);
        Set<TMMultiTapeConfiguration> configurations = new HashSet<>();
        configurations.add(tmMultiTapeConfiguration);
        assertEquals(null, tmMultiTapeConfiguration.getPrevious());
        assertEquals(q0, tmMultiTapeConfiguration.getState());
        assertArrayEquals(tapes, tmMultiTapeConfiguration2.getTapes());
        assertEquals("TMMultiTapeConfiguration{configuration=Configuration{previous=null, state=q0, depth=0}tapes=[aba, BBB], indexes=[0, 0]}",
                tmMultiTapeConfiguration.toString());
        assertTrue(configurations.contains(tmMultiTapeConfiguration2));
    }




}
